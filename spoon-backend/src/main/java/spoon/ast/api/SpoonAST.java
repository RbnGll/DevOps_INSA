package spoon.ast.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spoon.ast.impl.SpoonASTImpl;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;
import java.util.Optional;

@XmlSeeAlso(SpoonASTImpl.class)
public interface SpoonAST {
    @NotNull String getLabel();

    int getStartPosition();

    int getEndPosition();

    @NotNull String getTooltip();

    @NotNull List<SpoonAST> getChildren();

    void addChild(final SpoonAST node);

    void removeChild(final SpoonAST node);

    @NotNull Optional<SpoonAST> getParent();

    void setParent(final @Nullable SpoonAST node);
}
