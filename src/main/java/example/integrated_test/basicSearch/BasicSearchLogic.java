package example.integrated_test.basicSearch;

import example.integrated_test.util.ConfigUtil;
import example.integrated_test.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class BasicSearchLogic {
    private final ConfigUtil configUtil;
    private final RestHighLevelClient client;

    /**
     * "address" : {
     *             "cityName" : "suwon",
     *             "cityCode" : "03"
     *           },
     *  이런 형태일 때 cityName를 검색하려면 -> address.cityName
     * @param vo
     * @return
     * @throws IOException
     */
    public SearchResponse searchNoWildCard(String target, SearchVO vo) throws IOException {
        SearchRequest searchRequest = new SearchRequest(configUtil.indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(target, vo.getKeyword()));

        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);

        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
//        log.info(search.toString());
        return search;
    }

    public SearchResponse searchWildCard(String target, SearchVO vo) throws IOException {
        SearchRequest searchRequest = new SearchRequest(configUtil.indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.wildcardQuery(target, vo.getKeyword()));

        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);

        return client.search(searchRequest, RequestOptions.DEFAULT);
    }
}
