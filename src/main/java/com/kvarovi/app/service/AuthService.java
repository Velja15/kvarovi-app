package com.kvarovi.app.service;

import com.kvarovi.app.dto.request.LoginRequest;
import com.kvarovi.app.dto.request.RegisterRequest;
import com.kvarovi.app.dto.response.AuthResponse;
import com.kvarovi.app.entity.Building;
import com.kvarovi.app.entity.Role;
import com.kvarovi.app.entity.User;
import com.kvarovi.app.repository.BuildingRepository;
import com.kvarovi.app.repository.RoleRepository;
import com.kvarovi.app.repository.UserRepository;
import com.kvarovi.app.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BuildingRepository buildingRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BuildingRepository buildingRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.buildingRepository = buildingRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Korisnicko ime vec postoji");
        }

        Role stanarRole = roleRepository.findByName("ROLE_STANAR")
                .orElseThrow(() -> new RuntimeException("Uloga ROLE_STANAR ne postoji"));

        Building building = null;
        if (request.getBuildingId() != null) {
            building = buildingRepository.findById(request.getBuildingId())
                    .orElseThrow(() -> new RuntimeException("Zgrada nije pronadjena"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(stanarRole);
        user.setBuilding(building);

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), stanarRole.getName());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronadjen"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getRole().getName());
    }
}