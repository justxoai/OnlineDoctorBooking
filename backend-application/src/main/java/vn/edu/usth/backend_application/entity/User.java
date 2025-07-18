package vn.edu.usth.backend_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.usth.backend_application.enums.Gender;
import vn.edu.usth.backend_application.enums.Role;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_table")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "FullName", nullable = false)
    private String name;

    @Column(name = "Phone")
    private String phoneNumber;

    @Column(name = "Password")
    private String password;

    @Column(name = "DoB")
    private LocalDate birthday;

    @Column(name = "Gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    // JWT Authentication
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Connect to Doctor and Patient Entity
    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL)
    private Doctor doctor;

    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL)
    private Patient patient;

}
