package euclid.lyc_spring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Search", description = "검색 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc/searches")
public class SearchController {

/*-------------------------------------------------- 채팅 --------------------------------------------------*/

    @Operation(summary = "채팅을 주고받은 회원 검색하기", description = """
            검색어와 연관된(회원 이름이나 아이디와 일치) 채팅방 목록을 반환합니다.
            """)
    @GetMapping("/chats")
    public void searchChatMember(@RequestParam String keyword) {}


/*-------------------------------------------------- 피드 --------------------------------------------------*/

    @Operation(summary = "게시글 검색하기", description = """
            검색어와 연관된 게시글 목록을 반환합니다.
            
            연관도는 제목, 내용, 스타일 카테고리 등을 종합적으로 반영하여 결정됩니다.
            """)
    @GetMapping("/postings")
    public void searchPosting(@RequestParam String keyword) {}

/*-------------------------------------------------- 디렉터 --------------------------------------------------*/

    @Operation(summary = "일반 검색으로 디렉터 목록 불러오기", description = """
            검색어와 연관된(회원 이름이나 아이디와 일치) 디렉터 목록을 반환합니다.
            """)
    @GetMapping("/directors/general")
    public void searchDirectorByGenMode(@RequestParam String term) {}

    @Operation(summary = "키워드 검색으로 디렉터 목록 불러오기", description = """
            키워드는 유클리드에서 제공하는 스타일 테마 중 하나입니다.
            
            해당 스타일 테마를 선호하는 디렉터를 인기순, 최근활동순으로 반환합니다.
            """)
    @GetMapping("/directors/keyword")
    public void searchDirectorByKeywordMode(@RequestParam List<String> term) {}

}
