package io.github.okafke.aitcg.card.render;

// At some point we might need scaling?
public record CardFormat(int imageXOffset, int imageYOffset, int titleXOffset, int titleYOffset, int titleMaxWidth,
                         int textXOffset, int textYOffset, int textMaxWidth, int width, int height, int maxLines,
                         int cardSymbolX, int cardSymbolY) {
    public static CardFormat DEFAULT = new CardFormat(131, 214, 76, 106, 550, 100, 826, 569, 512, 512, 6, 58, 58);
    public static CardFormat SYMBOL = new CardFormat(131, 214, 132, 106, 494, 100, 826, 569, 512, 512, 6, 58, 58);

}
