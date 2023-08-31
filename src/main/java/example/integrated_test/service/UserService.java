package example.integrated_test.service;

import example.integrated_test.customException.DeleteException;
import example.integrated_test.customException.FindException;
import example.integrated_test.customException.SaveException;
import example.integrated_test.customException.UpdateException;
import example.integrated_test.domain.User;
import example.integrated_test.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(UserDTO dto) {
        User user = User.builder()
                .name(dto.getName()).phoneNumber(dto.getPhoneNumber())
                .build();

        try {
            redisTemplate.opsForHash().put("user", user.getName(), user.getPhoneNumber());
            log.info("save success");
        } catch (Exception e) {
            throw new SaveException("save fail");
        }
    }

    public JSONObject findAll() {
        return new JSONObject(redisTemplate.opsForHash().entries("user"));
    }

    public JSONObject findOneUser(String name) {
        try {
            Map<Object, Object> userMap = redisTemplate.opsForHash().entries("user");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(name, userMap.get(name));
            log.info("success finding current user");
            return jsonObject;
        } catch (Exception e) {
            throw new FindException("not found current user");
        }
    }

    public String findOne(String name) {
        try {
            Object value = redisTemplate.opsForHash().get("user", name);
            log.info("success finding current user of value");
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            throw new FindException("not found current user of value");
        }
    }

    public String delete(String name) {
        try {
            redisTemplate.opsForHash().delete("user", name);
            log.info("delete success");
            return "delete success";
        } catch (Exception e) {
            throw new DeleteException("delete fail");
        }
    }

    public String update(String name, JSONObject jsonObject) {
        try {
            redisTemplate.opsForHash().put("user", name, jsonObject.get("phoneNumber"));
            log.info("update success");
            return "update success";
        } catch (Exception e) {
            throw new UpdateException("update fail");
        }
    }
}
