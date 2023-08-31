package example.integrated_test.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "User")
@Getter
@NoArgsConstructor
public class User {
    @Id
    private String name;
    private String phoneNumber;

    @Builder
    public User(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
