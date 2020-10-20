package spoon.ast.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spoon.ast.api.SpoonAST;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestSpoonASTImpl {
    public SpoonAST spoonAST, parent, child;

    @BeforeEach
    void setUp() {
        spoonAST = new SpoonASTImpl("label", "tooltip", 0, 10);
        parent = new SpoonASTImpl("parent", "tooltiparent",0,100);
        child = new SpoonASTImpl("child","tooltipchild", 1,10);
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
        assertThat(spoonAST.getChildren()).isEqualTo(new ArrayList<>());
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
        SpoonAST secondParent = new SpoonASTImpl("secondParent","tooltip",2,3);
        spoonAST.setParent(parent);
        spoonAST.setParent(secondParent);
        assertThat(spoonAST.getParent()).isNotEmpty();
        assertThat(spoonAST.getParent().get()).isEqualTo(secondParent);
    }

    @Test
    void testSetParentWithChild() {
        SpoonAST secondParent = new SpoonASTImpl("secondParent","tooltip",2,3);
        spoonAST.setParent(parent);
        spoonAST.addChild(child);
        spoonAST.setParent(secondParent);
        assertThat(spoonAST.getParent()).isNotEmpty();
        assertThat(spoonAST.getParent().get()).isEqualTo(secondParent);
        assertThat(spoonAST.getChildren().size()).isEqualTo(1);
        assertThat(spoonAST.getChildren().get(0)).isEqualTo(child);
    }

    @Test
    void testAddChildOK() {
        spoonAST.setParent(parent);
        spoonAST.addChild(new SpoonASTImpl());
        assertThat(spoonAST.getChildren().size()).isEqualTo(1);
        assertThat(spoonAST.getChildren().get(0)).isEqualTo(child);
    }
}
