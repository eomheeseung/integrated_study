package example.integrated_test.controller;

import example.integrated_test.dto.UserDTO;
import example.integrated_test.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RedisMainController {
    private final UserService userService;

    //TODO
    // 1. ES7 사용해보자.
    // 2. webflux로 튜닝

    /**
     * @param dto
     */
    @Tag(name = "Save", description = "user 저장")
    @Operation(summary = "save user", description = "첫 번재 user를 저장합니다. 저장 방식은 name, phone number 입니다.")
    @PostMapping("docker/redis/save")
    public void save(@RequestBody UserDTO dto) {
        userService.save(dto);
    }

    /**
     * hash를 사용해서
     * Ex)
     * user kim 010-1111-1111
     * user lee 010-2222-2222
     * 와 같은 형식으로 저장하고 findAll할 수 있음
     *
     * @return
     */
    @Tag(name = "Find", description = "user 찾기")
    @Operation(summary = "find All", description = "저장된 모든 user를 찾습니다.")
    @PostMapping("docker/redis/find/all")
    public JSONObject findAll() {
        return userService.findAll();
    }

    @Tag(name = "Find", description = "user 찾기")
    @Operation(summary = "find one", description = "이름을 통해 한명의 user를 찾습니다.")
    @PostMapping("docker/redis/find/user/{name}")
    public JSONObject findOneUser(@PathVariable String name) {
        return userService.findOneUser(name);
    }

    @Tag(name = "Find", description = "user 찾기")
    @Operation(summary = "find one", description = "이름을 통해 한명의 user의 value(phone number)를 찾습니다.")
    @PostMapping("docker/redis/find/value/{name}")
    public String findOne(@PathVariable String name) {
        return userService.findOne(name);
    }


    @Tag(name = "Delete", description = "user 삭제")
    @Operation(summary = "delete", description = "입력된 이름의 user를 삭제합니다.")
    @PostMapping("docker/redis/delete/user/{name}")
    public String deleteOne(@PathVariable String name) {
        return userService.delete(name);
    }

    @Tag(name = "Update", description = "user 수정")
    @Operation(summary = "update", description = "이름을 통해 찾은 user의 정보를 수정합니다.")
    @PostMapping("docker/redis/update/user/{name}")
    public String updateOne(@PathVariable String name, @RequestBody JSONObject jsonObject) {
        return userService.update(name, jsonObject);
    }
}
