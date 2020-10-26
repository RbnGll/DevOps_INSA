package spoon.ast.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import spoon.ast.api.SpoonAST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestSpoonASTImpl {
    public SpoonAST spoonAST, parent, child;

    @BeforeEach
    void setUp() {
        spoonAST = new SpoonASTImpl("label", "tooltip", 0, 10);
        parent = new SpoonASTImpl("parent", "tooltiparent", 0, 100);
        child = new SpoonASTImpl("child", "tooltipchild", 1, 10);
    }
    private static Stream <Arguments> provideToStringParameters() {
        SpoonAST spoonAST = new SpoonASTImpl("label", "tooltip", 0, 10);
        SpoonAST parent = new SpoonASTImpl("parent", "tooltip", 1, 9);
        parent.addChild(spoonAST);

        return Stream.of(
                Arguments.of(spoonAST, "SpoonASTImpl[label=label, tooltip='tooltip', startPosition=0, endPosition=10, children=[]]"),
                Arguments.of(parent, "SpoonASTImpl[label=parent, tooltip='tooltip', startPosition=1, endPosition=9, children=[SpoonASTImpl[label=label, tooltip='tooltip', startPosition=0, endPosition=10, children=[]]]]")
        );
    }

     // @Test
     // void testBaseConstructor() {
     //     spoonAST = new SpoonASTImpl();
     //     assertThat(spoonAST.getLabel()).isNull();
     //     assertThat(spoonAST.getTooltip()).isNull();
     //     assertThat(spoonAST.getStartPosition()).isEqualTo(0);
     //     assertThat(spoonAST.getEndPosition()).isEqualTo(0);
     //     assertThrows(NullPointerException.class, ()->spoonAST.getChildren());
     // }

    @Test
    void testGetLabel() {
        assertThat(spoonAST.getLabel()).isEqualTo("label");
    }

    @Test
    void testGetTooltip() {
        assertThat(spoonAST.getTooltip()).isEqualTo("tooltip");
    }

    @Test
    void testGetStartPosition() {
        assertThat(spoonAST.getStartPosition()).isEqualTo(0);
    }

    @Test
    void testGetEndPosition() {
        assertThat(spoonAST.getEndPosition()).isEqualTo(10);
    }

    @Test
    void testGetParent() {
        assertThat(spoonAST.getParent()).isEmpty();
    }

    @Test
    void testGetChildren() {
        assertThat(spoonAST.getChildren()).isExactlyInstanceOf(Collections.unmodifiableList(new ArrayList<>()).getClass());
        assertThat(spoonAST.getChildren()).isEmpty();
    }

    @Test
    void testGetAddChildren() {
        assertThrows(UnsupportedOperationException.class, ()->spoonAST.getChildren().add(child));
    }

    @Test
    void testSetParentOK() {
        spoonAST.setParent(parent);
        assertThat(spoonAST.getParent()).isNotEmpty();
        assertThat(spoonAST.getParent().get()).isEqualTo(parent);
    }

    @Test
    void testSetNullParent() {
        spoonAST.setParent(parent);
        spoonAST.setParent(null);
        assertThat(spoonAST.getParent()).isEmpty();

    }

    @Test
    void testAlreadySetParent() {
        SpoonAST secondParent = new SpoonASTImpl("secondParent", "tooltip", 2, 3);
        spoonAST.setParent(parent);
        spoonAST.setParent(secondParent);
        assertThat(spoonAST.getParent()).isNotEmpty();
        assertThat(spoonAST.getParent().get()).isEqualTo(secondParent);
    }

    @Test
    void testSetParentWithChild() {
        SpoonAST secondParent = new SpoonASTImpl("secondParent", "tooltip", 2, 3);
        spoonAST.setParent(parent);
        spoonAST.addChild(child);
        spoonAST.setParent(secondParent);
        assertThat(spoonAST.getParent()).isNotEmpty();
        assertThat(spoonAST.getParent().get()).isEqualTo(secondParent);
        assertThat(spoonAST.getChildren().size()).isEqualTo(1);
        assertThat(spoonAST.getChildren().get(0)).isEqualTo(child);
    }


    @Test
    void testAddChildOk() {
        spoonAST.addChild(child);
        assertThat(spoonAST.getChildren().size()).isEqualTo(1);
        assertThat(spoonAST.getChildren().get(0)).isEqualTo(child);
        assertThat(child.getParent()).isPresent();
        assertThat(child.getParent().get()).isEqualTo(spoonAST);
    }

    @Test
    void testAddChildren() {
        List<SpoonAST> children = Stream.of(
                child,
                new SpoonASTImpl("child2", "tooltipchild2", 2, 10),
                new SpoonASTImpl("child3", "tooltipchild3", 3, 10)
        ).collect(Collectors.toList());

        children.forEach(spoonAST1 -> spoonAST.addChild(spoonAST1));
        assertThat(spoonAST.getChildren().size()).isEqualTo(3);
        children.forEach(spoonAST1 -> assertThat(spoonAST1.getParent()).isPresent());
        children.forEach(spoonAST1 -> assertThat(spoonAST1.getParent().get()).isEqualTo(spoonAST));
    }

    @Test
    void testAddNullChild() {
        assertThrows(IllegalArgumentException.class, () -> spoonAST.addChild(null));
    }

    @Test
    void testAddAlreadyAddedChild() {
        spoonAST.addChild(child);
        spoonAST.addChild(child);
        assertThat(spoonAST.getChildren().size()).isEqualTo(1);
        assertThat(child.getChildren().size()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("provideToStringParameters")
    void testToString(SpoonAST spoonAST, String expectedString) {
        assertThat(spoonAST.toString()).isEqualTo(expectedString);
    }

    @Test
    void testRemoveChildOk() {
        parent.addChild(child);
        parent.removeChild(child);
        assertThat(parent.getChildren()).isEmpty();
        assertThat(child.getParent()).isEmpty();
    }

    @Test
    void testRemoveNotPresentChild() {
        assertThat(parent.getChildren()).isEmpty();
        parent.removeChild(child);
        assertThat(parent.getChildren()).isEmpty();
    }
}
