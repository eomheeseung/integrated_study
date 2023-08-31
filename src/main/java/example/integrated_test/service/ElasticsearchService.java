package example.integrated_test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.integrated_test.customException.CreateDocumentException;
import example.integrated_test.customException.CreateIndexException;
import example.integrated_test.customException.DeleteDocumentException;
import example.integrated_test.customException.InquiryDocumentException;
import example.integrated_test.dto.OrderDTO;
import example.integrated_test.util.ConfigUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * create index service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchService {
    private final RestHighLevelClient client;
    private static final String sourceDetail = "{\"index\":{\"number_of_shards\":1,\"number_of_replicas\":0}}";
    private final ConfigUtil configUtil;

    /**
     * es를 사용하기 위해서 처음에 index create
     */
    public void createIndex() {
        CreateIndexRequest request = new CreateIndexRequest(configUtil.indexName);

        // client로 부터 오는 source settings
        request.settings(Settings.builder()
                .loadFromSource(sourceDetail, XContentType.JSON).build());

        try {
            client.indices().create(request, RequestOptions.DEFAULT);
            log.info("success elasticsearch create index");
        } catch (IOException e) {
            throw new CreateIndexException("fail to elasticsearch create index");
        }
    }

    /**
     * save document
     * @param dto
     */
    public void createDocument(OrderDTO dto) {
        ObjectMapper mapper = new ObjectMapper();

        IndexRequest request  = new IndexRequest(configUtil.indexName)
                .id(dto.getOrderId().toString())
                .source(mapper.convertValue(dto, Map.class), XContentType.JSON);

        try {
            client.index(request, RequestOptions.DEFAULT);
            log.info("success save document");
        } catch (IOException e) {
            throw new CreateDocumentException("fila to save document");
        }
    }

    /**
     * inquiry document
     */
    public GetResponse getDocument(String findOrderId) {
        GetRequest request = new GetRequest(configUtil.indexName, findOrderId);

        try {
            return client.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new InquiryDocumentException("not found document");
        }
    }

    /**
     * delete document
     */
    public void deleteDocument(String findOrderId) {
        DeleteRequest request = new DeleteRequest(configUtil.indexName, findOrderId);
        try {
            client.delete(request, RequestOptions.DEFAULT);
            log.info("success delete document");
        } catch (IOException e) {
            throw new DeleteDocumentException("fail to delete documnet");
        }
    }
}
