package com.B2007186.AdviseNutrition.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.DateFormat;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user_table", uniqueConstraints = @UniqueConstraint(columnNames = "userName"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id @GeneratedValue
    @Column(name = "user_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false, unique = true)
    private String passWord;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private DateFormat Birth;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isActive;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Token> tokens;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user")
    private DoctorLicense license;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Post> post;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Comment> comment;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return userName;
    }
    @Override
    public String getPassword() {
        return passWord;
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
}
