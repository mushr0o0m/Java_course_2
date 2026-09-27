package MyFirstTestAppSpringBoot.hello;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HelloControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    void helloUsesWorldByDefault() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }

    @Test
    void helloUsesNameFromQueryParameter() throws Exception {
        mockMvc.perform(get("/hello").param("name", "Petya"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Petya!"));
    }

    @Test
    void arrayEndpointsKeepAddedValues() throws Exception {
        mockMvc.perform(get("/update-array").param("s", "Java"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Java")));
        mockMvc.perform(get("/update-array").param("s", "Spring"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/show-array"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("Java"))
                .andExpect(jsonPath("$[1]").value("Spring"));
    }

    @Test
    void mapEndpointsKeepAddedValues() throws Exception {
        mockMvc.perform(get("/update-map").param("s", "first"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/update-map").param("s", "second"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/show-map"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.0").value("first"))
                .andExpect(jsonPath("$.1").value("second"));
    }

    @Test
    void sizeEndpointReportsBothCollections() throws Exception {
        mockMvc.perform(get("/update-array").param("s", "one"));
        mockMvc.perform(get("/update-map").param("s", "two"));

        mockMvc.perform(get("/show-all-lenght"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Количество элементов в ArrayList: 1, количество элементов в HashMap: 1"));
    }
}