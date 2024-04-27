package com.B2007186.AdviseNutrition.domain.Users;

import com.B2007186.AdviseNutrition.domain.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user_table", uniqueConstraints = @UniqueConstraint(columnNames = "userName"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class User implements UserDetails {
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
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate Birth;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private Boolean postPermit;

    private Boolean enabled;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Token> tokens;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> post;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> comment;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes;


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
        return enabled;
    }

    public CharSequence getFullName() {
        return firstName + " " + lastName;
    }
}
