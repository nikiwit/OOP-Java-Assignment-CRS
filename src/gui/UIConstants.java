package gui;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Centralized UI constants for consistent design across the application.
 * Defines colors, fonts, spacing, and other design tokens.
 */
public class UIConstants {

    // ============================================
    // COLOR PALETTE
    // ============================================

    // Primary Colors
    public static final Color PRIMARY = new Color(52, 152, 219);           // Professional Blue
    public static final Color PRIMARY_DARK = new Color(41, 128, 185);      // Darker Blue
    public static final Color PRIMARY_LIGHT = new Color(174, 214, 241);    // Light Blue

    // Semantic Colors
    public static final Color SUCCESS = new Color(46, 204, 113);           // Green
    public static final Color SUCCESS_DARK = new Color(39, 174, 96);       // Dark Green
    public static final Color WARNING = new Color(243, 156, 18);           // Orange
    public static final Color WARNING_DARK = new Color(230, 126, 34);      // Dark Orange
    public static final Color DANGER = new Color(231, 76, 60);             // Red
    public static final Color DANGER_DARK = new Color(192, 57, 43);        // Dark Red
    public static final Color INFO = new Color(155, 89, 182);              // Purple
    public static final Color INFO_DARK = new Color(142, 68, 173);         // Dark Purple

    // Neutral Colors
    public static final Color BACKGROUND = new Color(236, 240, 241);       // Light Gray
    public static final Color BACKGROUND_WHITE = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);        // Dark Gray
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);   // Medium Gray
    public static final Color TEXT_LIGHT = new Color(189, 195, 199);       // Light Gray

    // Sidebar & Navigation
    public static final Color SIDEBAR_BG = new Color(52, 73, 94);          // Dark Blue-Gray
    public static final Color SIDEBAR_HOVER = new Color(70, 100, 120);     // Lighter on hover
    public static final Color SIDEBAR_ACTIVE = PRIMARY;                     // Active item

    // Table Colors
    public static final Color TABLE_HEADER = PRIMARY;
    public static final Color TABLE_ROW_EVEN = Color.WHITE;
    public static final Color TABLE_ROW_ODD = new Color(249, 249, 249);
    public static final Color TABLE_HOVER = new Color(240, 248, 255);
    public static final Color TABLE_SELECTED = PRIMARY_LIGHT;

    // Status Colors (for grading)
    public static final Color STATUS_PASSED = new Color(230, 255, 230);    // Light Green
    public static final Color STATUS_FAILED = new Color(255, 230, 230);    // Light Red
    public static final Color STATUS_TRANSIT = new Color(255, 250, 205);   // Light Yellow
    public static final Color STATUS_INCOMPLETE = new Color(245, 245, 245);// Light Gray

    // Border Colors
    public static final Color BORDER_LIGHT = new Color(220, 220, 220);
    public static final Color BORDER_MEDIUM = new Color(200, 200, 200);
    public static final Color BORDER_DARK = new Color(150, 150, 150);

    // ============================================
    // TYPOGRAPHY
    // ============================================

    public static final String FONT_FAMILY = "Arial";
    public static final String FONT_FAMILY_FALLBACK = Font.SANS_SERIF;

    // Font Sizes
    public static final int FONT_SIZE_H1 = 24;      // Main titles
    public static final int FONT_SIZE_H2 = 20;      // Section headers
    public static final int FONT_SIZE_H3 = 18;      // Subsection headers
    public static final int FONT_SIZE_BODY = 14;    // Normal text
    public static final int FONT_SIZE_SMALL = 12;   // Small text
    public static final int FONT_SIZE_TINY = 10;    // Tiny text

    // Font Weights
    public static final int FONT_WEIGHT_NORMAL = Font.PLAIN;
    public static final int FONT_WEIGHT_BOLD = Font.BOLD;

    // Font Presets
    public static final Font FONT_H1 = new Font(FONT_FAMILY, FONT_WEIGHT_BOLD, FONT_SIZE_H1);
    public static final Font FONT_H2 = new Font(FONT_FAMILY, FONT_WEIGHT_BOLD, FONT_SIZE_H2);
    public static final Font FONT_H3 = new Font(FONT_FAMILY, FONT_WEIGHT_BOLD, FONT_SIZE_H3);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, FONT_WEIGHT_NORMAL, FONT_SIZE_BODY);
    public static final Font FONT_BODY_BOLD = new Font(FONT_FAMILY, FONT_WEIGHT_BOLD, FONT_SIZE_BODY);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, FONT_WEIGHT_NORMAL, FONT_SIZE_SMALL);
    public static final Font FONT_BUTTON = new Font(FONT_FAMILY, FONT_WEIGHT_BOLD, FONT_SIZE_BODY);

    // ============================================
    // SPACING
    // ============================================

    public static final int SPACING_XS = 5;
    public static final int SPACING_SM = 10;
    public static final int SPACING_MD = 15;
    public static final int SPACING_LG = 20;
    public static final int SPACING_XL = 30;
    public static final int SPACING_XXL = 40;

    // Component Spacing
    public static final Insets INSETS_NONE = new Insets(0, 0, 0, 0);
    public static final Insets INSETS_XS = new Insets(SPACING_XS, SPACING_XS, SPACING_XS, SPACING_XS);
    public static final Insets INSETS_SM = new Insets(SPACING_SM, SPACING_SM, SPACING_SM, SPACING_SM);
    public static final Insets INSETS_MD = new Insets(SPACING_MD, SPACING_MD, SPACING_MD, SPACING_MD);
    public static final Insets INSETS_LG = new Insets(SPACING_LG, SPACING_LG, SPACING_LG, SPACING_LG);

    // ============================================
    // SIZES
    // ============================================

    // Button Sizes
    public static final int BUTTON_HEIGHT = 40;
    public static final int BUTTON_HEIGHT_SMALL = 32;
    public static final int BUTTON_MIN_WIDTH = 100;

    // Input Sizes
    public static final int INPUT_HEIGHT = 36;
    public static final int INPUT_MIN_WIDTH = 200;

    // Sidebar
    public static final int SIDEBAR_WIDTH = 220;
    public static final int SIDEBAR_BUTTON_HEIGHT = 50;

    // Header
    public static final int HEADER_HEIGHT = 70;

    // Table
    public static final int TABLE_ROW_HEIGHT = 35;
    public static final int TABLE_HEADER_HEIGHT = 40;

    // Card
    public static final int CARD_MIN_HEIGHT = 120;
    public static final int CARD_MIN_WIDTH = 200;

    // Window
    public static final Dimension WINDOW_MIN_SIZE = new Dimension(1000, 700);
    public static final Dimension LOGIN_WINDOW_SIZE = new Dimension(450, 550);

    // ============================================
    // BORDERS & EFFECTS
    // ============================================

    public static final int BORDER_RADIUS = 4;      // Standard corner radius
    public static final int BORDER_THICKNESS = 1;
    public static final int BORDER_THICK = 2;
    public static final int BORDER_CARD = 3;

    // Border Presets
    public static final Border BORDER_EMPTY_SM = BorderFactory.createEmptyBorder(
        SPACING_SM, SPACING_SM, SPACING_SM, SPACING_SM);
    public static final Border BORDER_EMPTY_MD = BorderFactory.createEmptyBorder(
        SPACING_MD, SPACING_MD, SPACING_MD, SPACING_MD);
    public static final Border BORDER_EMPTY_LG = BorderFactory.createEmptyBorder(
        SPACING_LG, SPACING_LG, SPACING_LG, SPACING_LG);

    public static final Border BORDER_LINE = BorderFactory.createLineBorder(BORDER_LIGHT, BORDER_THICKNESS);
    public static final Border BORDER_LINE_MEDIUM = BorderFactory.createLineBorder(BORDER_MEDIUM, BORDER_THICKNESS);
    public static final Border BORDER_LINE_DARK = BorderFactory.createLineBorder(BORDER_DARK, BORDER_THICKNESS);

    // ============================================
    // ANIMATION & TIMING
    // ============================================

    public static final int ANIMATION_DURATION_SHORT = 150;  // milliseconds
    public static final int ANIMATION_DURATION_MEDIUM = 300;
    public static final int ANIMATION_DURATION_LONG = 500;

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Creates a lighter version of the given color.
     * @param color the base color
     * @param factor lightness factor (0.0 to 1.0)
     * @return lighter color
     */
    public static Color lighter(Color color, float factor) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        int nr = Math.min(255, (int)(r + (255 - r) * factor));
        int ng = Math.min(255, (int)(g + (255 - g) * factor));
        int nb = Math.min(255, (int)(b + (255 - b) * factor));

        return new Color(nr, ng, nb);
    }

    /**
     * Creates a darker version of the given color.
     * @param color the base color
     * @param factor darkness factor (0.0 to 1.0)
     * @return darker color
     */
    public static Color darker(Color color, float factor) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        int nr = Math.max(0, (int)(r * (1 - factor)));
        int ng = Math.max(0, (int)(g * (1 - factor)));
        int nb = Math.max(0, (int)(b * (1 - factor)));

        return new Color(nr, ng, nb);
    }

    /**
     * Creates an alpha-blended version of the given color.
     * @param color the base color
     * @param alpha transparency (0-255)
     * @return color with alpha
     */
    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    /**
     * Creates a compound border with padding.
     * @param lineColor border line color
     * @param padding internal padding
     * @return compound border
     */
    public static Border createPaddedBorder(Color lineColor, int padding) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(lineColor, BORDER_THICKNESS),
            BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        );
    }

    /**
     * Creates a card-style border.
     * @param color border color
     * @return card border
     */
    public static Border createCardBorder(Color color) {
        return BorderFactory.createLineBorder(color, BORDER_CARD);
    }
}
