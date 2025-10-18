package io.zhc1.realworld.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import io.zhc1.realworld.model.Tag;
import io.zhc1.realworld.service.TagService;

@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TagController 테스트")
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Test
    @DisplayName("GET /api/tags - 모든 태그를 조회한다")
    void getAllTags_ShouldReturnAllTags() throws Exception {
        // given
        Tag tag1 = new Tag("java");
        Tag tag2 = new Tag("spring");
        Tag tag3 = new Tag("testing");

        given(tagService.getAllTags()).willReturn(List.of(tag1, tag2, tag3));

        // when & then
        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags.length()").value(3))
                .andExpect(jsonPath("$.tags[0]").value("java"))
                .andExpect(jsonPath("$.tags[1]").value("spring"))
                .andExpect(jsonPath("$.tags[2]").value("testing"));
    }

    @Test
    @DisplayName("GET /api/tags - 태그가 없을 때 빈 배열을 반환한다")
    void getAllTags_WhenNoTags_ShouldReturnEmptyArray() throws Exception {
        // given
        given(tagService.getAllTags()).willReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags.length()").value(0));
    }
}
