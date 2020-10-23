package spoon.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TestCodeDTO {
    public CodeDTO codeDTO;

    @BeforeEach
    void setUp() {
        codeDTO = new CodeDTO("public class foo{}","level");
    }

    @Test
    void testGetCode() {
        assertThat(codeDTO.getCode()).isEqualTo("public class foo{}");
    }

    @Test
    void testSetCode() {
        codeDTO.setCode("empty");
        assertThat(codeDTO.getCode()).isEqualTo("empty");
    }

    @Test
    void testGetLevel() {
        assertThat(codeDTO.getLevel()).isEqualTo("level");
    }

    @Test
    void testSetLevel() {
        codeDTO.setLevel("2");
        assertThat(codeDTO.getLevel()).isEqualTo("2");
    }
}
