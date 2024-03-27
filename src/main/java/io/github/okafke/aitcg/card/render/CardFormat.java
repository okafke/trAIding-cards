package io.github.okafke.aitcg.card.render;

// At some point we might need scaling?
public record CardFormat(int imageXOffset, int imageYOffset, int titleXOffset, int titleYOffset, int titleMaxWidth,
                         int textXOffset, int textYOffset, int textMaxWidth, int width, int height, int maxLines,
                         int cardSymbolX, int cardSymbolY, int symbolFontSize1, int symbolFontSize2, int titleFontSize,
                         int textFontSize, int statsFontSize, int statsMultiplier /* TODO: lazy!*/) {
    public static CardFormat DEFAULT = new CardFormat(
            131, 214, 76, 106, 550, 100, 826, 569,
            512, 512, 6, 58, 58, 44, 42, 40, 17, 20, 1);
    public static CardFormat SYMBOL = new CardFormat(
            131, 214, 132, 106, 494, 100, 826, 569,
            512, 512, 6, 58, 58, 44, 42, 40, 17, 20, 1);
    public static CardFormat SYMBOL2X = new CardFormat(
            131 * 2, 214 * 2, 132 * 2, 106 * 2, 494 * 2, 100 * 2, 826 * 2, 569 * 2,
            512 * 2, 512 * 2, 6, 58 * 2, 58 * 2, 44 * 2, 42 * 2, 40 * 2, 17 * 2, 20 * 2, 2);
    public static CardFormat DEFAULT2X = new CardFormat(
            131 * 2, 214 * 2, 76 * 2, 106 * 2, 550 * 2, 100 * 2, 826 * 2, 569 * 2,
            512 * 2, 512 * 2, 6, 58 * 2, 58 * 2, 44 * 2, 42 * 2, 40 * 2, 17 * 2, 20 * 2, 2);

}
