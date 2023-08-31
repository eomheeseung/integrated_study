package example.integrated_test.controller;

import example.integrated_test.customException.CreateDocumentException;
import example.integrated_test.dto.OrderDTO;
import example.integrated_test.service.ElasticsearchService;
import example.integrated_test.vo.OrderIdVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ESMainController {
    private final ElasticsearchService elasticsearchService;
    private long orderId = 1;


    @Tag(name = "Elasticsearch", description = "Elasticsearch CRUD")
    @Operation(summary = "ES create index", description = "사용하기 위해서 먼저 index를 생성해야 한다.")
    @GetMapping("/es/create/index")
    public void save() {
        elasticsearchService.createIndex();
    }

    @Tag(name = "Elasticsearch", description = "Elasticsearch CRUD")
    @Operation(summary = "create order", description = "실제 데이터를 client로부터 받아서 order에 해당하는 document 생성")
    @PostMapping("/es/create/order")
    public String saveOrder(@RequestBody OrderDTO orderDTO) {
        orderDTO.setOrderId(orderId++);
        orderDTO.setOrderDate(new Date());

        try {
            elasticsearchService.createDocument(orderDTO);
            return "save success";
        } catch (CreateDocumentException e) {
            return "save fail";
        }
    }

    /**
     * TODO
     *  1. 찾는 order가 없다면??
     *  2. orderId도 따로 묶어서 보관이 필요할 거 같은데..
     * @param vo
     * @return
     */
    @Tag(name = "Elasticsearch", description = "Elasticsearch CRUD")
    @Operation(summary = "inquiry order", description = "저장된 order들을 조회")
    @PostMapping("es/inquiry/order")
    public JSONObject inquiryDocument(@RequestBody OrderIdVO vo) {
        GetResponse response = elasticsearchService.getDocument(vo.getOrderId());
        Map<String, Object> sourceAsMap = response.getSourceAsMap();

        JSONObject jsonObject = new JSONObject(sourceAsMap);

        log.info(jsonObject.toJSONString());
        return jsonObject;
    }

    @Tag(name = "Elasticsearch", description = "Elasticsearch CRUD")
    @Operation(summary = "delete order", description = "저장된 order을 삭제")
    @DeleteMapping("es/delete/order")
    public String deleteDocument(@RequestBody OrderIdVO vo) {
        elasticsearchService.deleteDocument(vo.getOrderId());
        return "delete success";
    }
}