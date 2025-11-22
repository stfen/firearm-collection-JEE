package com.firearms.firearmcollectionjee.configuration.singleton;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.User;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.entity.enums.AmmoType;
import com.firearms.firearmcollectionjee.entity.enums.UserRoles;
import com.firearms.firearmcollectionjee.repository.impl.FirearmRepository;
import com.firearms.firearmcollectionjee.repository.impl.UserRepository;
import com.firearms.firearmcollectionjee.repository.impl.WeaponFamilyRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Singleton
@Startup
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
@NoArgsConstructor(force = true)
@DependsOn("InitializeAdminService")
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
@RunAs(UserRoles.ADMIN)
@Log
public class InitializedData {
        private final UserRepository userRepository;
        private final WeaponFamilyRepository weaponFamilyRepository;
        private final FirearmRepository firearmRepository;
        private final Pbkdf2PasswordHash passwordHash;

        @Inject
        public InitializedData(UserRepository userRepository, 
                                                  WeaponFamilyRepository weaponFamilyRepository,
                                                  FirearmRepository firearmRepository,
                                                  @SuppressWarnings("CdiInjectionPointsInspection") Pbkdf2PasswordHash passwordHash) {
                this.userRepository = userRepository;
                this.weaponFamilyRepository = weaponFamilyRepository;
                this.firearmRepository = firearmRepository;
                this.passwordHash = passwordHash;
        }

    @SneakyThrows
    @PostConstruct
    private void init() {
        if (userRepository.findByLogin("admin").isEmpty()) {
            User user1 = User.builder()
                    .id(UUID.fromString("178960ce-f5bf-4e54-82f3-8b10a69d7cce"))
                    .login("admin")
                    .email("ziomus@example.com")
                    .password(passwordHash.generate("useruser".toCharArray()))
                    .roles(List.of(UserRoles.ADMIN))
                    .build();

            User regularUser = User.builder()
                    .id(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                    .login("user")
                    .birthDate(LocalDate.of(1995, 5, 15))
                    .email("user@firearmcollection.example.com")
                    .password(passwordHash.generate("useruser".toCharArray()))
                    .roles(List.of(UserRoles.USER))
                    .build();

            User user2 = User.builder()
                    .id(UUID.fromString("c461d210-8cea-4213-a19a-3cb856351d56"))
                    .login("uzytnik2")
                    .email("uzytnik2@example.com")
                    .password(passwordHash.generate("useruser".toCharArray()))
                    .roles(List.of(UserRoles.USER))
                    .build();

            User user3 = User.builder()
                    .id(UUID.fromString("70b21553-3690-4464-97a3-4481ce862b28"))
                    .login("gracz2")
                    .email("gracz2@example.com")
                    .password(passwordHash.generate("useruser".toCharArray()))
                    .roles(List.of(UserRoles.USER))
                    .build();

            User user4 = User.builder()
                    .id(UUID.fromString("70b21512-1234-5678-97a3-4481ce862b26"))
                    .login("seima")
                    .email("seima@example.com")
                    .password(passwordHash.generate("useruser".toCharArray()))
                    .roles(List.of(UserRoles.USER))
                    .build();

            userRepository.create(user1);
            userRepository.create(user2);
            userRepository.create(user3);
            userRepository.create(user4);
            userRepository.create(regularUser);

            // Create WeaponFamilies
            WeaponFamily assaultRifle = WeaponFamily.builder()
                    .id(UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890"))
                    .name("Assault Rifle")
                    .optimalRange(400)
                    .ammoType(AmmoType.BULLET)
                    .build();

            WeaponFamily shotgun = WeaponFamily.builder()
                    .id(UUID.fromString("b2c3d4e5-f6a7-8901-bcde-f23456789012"))
                    .name("Shotgun")
                    .optimalRange(50)
                    .ammoType(AmmoType.SHELL)
                    .build();

            WeaponFamily sniper = WeaponFamily.builder()
                    .id(UUID.fromString("c3d4e5f6-a7b8-9012-cdef-345678901234"))
                    .name("Sniper Rifle")
                    .optimalRange(800)
                    .ammoType(AmmoType.BULLET)
                    .build();

            WeaponFamily pistol = WeaponFamily.builder()
                    .id(UUID.fromString("e5f6a7b8-c9d0-1234-efab-567890123456"))
                    .name("Pistol")
                    .optimalRange(100)
                    .ammoType(AmmoType.BULLET)
                    .build();

            weaponFamilyRepository.create(assaultRifle);
            weaponFamilyRepository.create(shotgun);
            weaponFamilyRepository.create(sniper);
            weaponFamilyRepository.create(pistol);

            // Create Firearms
            Firearm ak47 = Firearm.builder()
                    .id(UUID.fromString("11111111-2222-3333-4444-555555555555"))
                    .name("AK-47")
                    .caliber(7.62)
                    .magazineCapacity(30)
                    .weaponFamily(assaultRifle)
                    .productionDate(LocalDate.of(1974, 5, 15))
                    .user(user1)
                    .build();

            Firearm m16 = Firearm.builder()
                    .id(UUID.fromString("22222222-3333-4444-5555-666666666666"))
                    .name("M16")
                    .caliber(5.56)
                    .magazineCapacity(30)
                    .weaponFamily(assaultRifle)
                    .productionDate(LocalDate.of(1964, 3, 20))
                    .user(user2)
                    .build();

            Firearm remington870 = Firearm.builder()
                    .id(UUID.fromString("33333333-4444-5555-6666-777777777777"))
                    .name("Remington 870")
                    .caliber(12.0)
                    .magazineCapacity(7)
                    .weaponFamily(shotgun)
                    .productionDate(LocalDate.of(1950, 8, 10))
                    .user(user1)
                    .build();

            Firearm barrett50 = Firearm.builder()
                    .id(UUID.fromString("44444444-5555-6666-7777-888888888888"))
                    .name("Barrett M82")
                    .caliber(12.7)
                    .magazineCapacity(10)
                    .weaponFamily(sniper)
                    .productionDate(LocalDate.of(1989, 11, 30))
                    .user(user3)
                    .build();

            Firearm glock17 = Firearm.builder()
                    .id(UUID.fromString("55555555-6666-7777-8888-999999999999"))
                    .name("Glock 17")
                    .caliber(9.0)
                    .magazineCapacity(17)
                    .weaponFamily(pistol)
                    .productionDate(LocalDate.of(1982, 7, 8))
                    .user(user4)
                    .build();

            Firearm colt1911 = Firearm.builder()
                    .id(UUID.fromString("66666666-7777-8888-9999-aaaaaaaaaaaa"))
                    .name("Colt 1911")
                    .caliber(0.45)
                    .magazineCapacity(7)
                    .weaponFamily(pistol)
                    .productionDate(LocalDate.of(1911, 1, 1))
                    .user(user2)
                    .build();

            firearmRepository.create(ak47);
            firearmRepository.create(m16);
            firearmRepository.create(remington870);
            firearmRepository.create(barrett50);
            firearmRepository.create(glock17);
            firearmRepository.create(colt1911);
        }
    }

    @SneakyThrows
    private byte[] getResourceAsByteArray(String path) {
        try (InputStream is = this.getClass().getResourceAsStream(path)) {
            if (is != null) {
                return is.readAllBytes();
            } else {
                throw new IllegalStateException("Unable to get resource %s".formatted(path));
            }
        }
    }

}
