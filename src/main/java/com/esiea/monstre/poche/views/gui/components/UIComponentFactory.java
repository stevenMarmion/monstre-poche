package com.esiea.monstre.poche.views.gui.components;

import com.esiea.monstre.poche.views.gui.config.ColorConfig;
import com.esiea.monstre.poche.views.gui.config.FontConfig;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Factory pour créer des composants UI standardisés avec styles cohérents.
 * Centralise la création de boutons, cartes et autres éléments UI pour éviter la duplication.
 */
public abstract class UIComponentFactory {

    /**
     * Crée un bouton d'action stylisé avec effets hover et pressed.
     * Utilisé pour les boutons principaux (Attaque, Sac, Pokemon).
     *
     * @param text Le texte du bouton
     * @param baseColor La couleur de fond principale (ex: "#e85858")
     * @param darkColor La couleur de la bordure/ombre (ex: "#c84848")
     * @param width Largeur du bouton
     * @param height Hauteur du bouton
     * @return Button stylisé avec effets
     */
    public static Button createActionButton(String text, String baseColor, String darkColor, int width, int height) {
        Button btn = new Button(text);
        btn.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 12));
        btn.setPrefWidth(width);
        btn.setPrefHeight(height);

        String normalStyle = String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 0 0 4 0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;",
            baseColor, darkColor
        );
        btn.setStyle(normalStyle);

        // Effet hover : éclaircir la couleur
        btn.setOnMouseEntered(e -> btn.setStyle(String.format(
            "-fx-background-color: derive(%s, 12%%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 0 0 4 0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;",
            baseColor, darkColor
        )));

        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));

        // Effet pressed : inverser la bordure
        btn.setOnMousePressed(e -> btn.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 4 0 0 0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;",
            darkColor, darkColor
        )));

        btn.setOnMouseReleased(e -> btn.setStyle(normalStyle));

        return btn;
    }

    /**
     * Crée un bouton d'action avec taille standard (105x42).
     */
    public static Button createActionButton(String text, String baseColor, String darkColor) {
        return createActionButton(text, baseColor, darkColor, 105, 42);
    }

    /**
     * Crée un bouton "RETOUR" standard grisé.
     *
     * @return Button de retour stylisé
     */
    public static Button createBackButton() {
        Button btn = new Button("RETOUR");
        btn.setPrefWidth(80);
        btn.setPrefHeight(32);
        btn.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, 10));
        btn.setStyle(
            "-fx-background-color: #606070; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 6; " +
            "-fx-border-color: #505060; " +
            "-fx-border-width: 0 0 2 0; " +
            "-fx-border-radius: 6; " +
            "-fx-cursor: hand;"
        );
        return btn;
    }

    /**
     * Crée un bouton stylisé avec couleur de type (pour attaques, objets, etc.).
     * Avec effets hover automatiques et bordure 3D.
     *
     * @param text Le texte du bouton
     * @param typeColor La couleur du type (ex: "#e85858" pour Feu)
     * @param width Largeur du bouton
     * @param height Hauteur du bouton
     * @param fontSize Taille de la police
     * @return Button stylisé avec effets hover
     */
    public static Button createTypeButton(String text, String typeColor, int width, int height, int fontSize) {
        Button btn = new Button(text);
        btn.setPrefWidth(width);
        btn.setPrefHeight(height);
        btn.setFont(Font.font(FontConfig.SYSTEM.getFontName(), FontWeight.BOLD, fontSize));

        String normalStyle = String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: derive(%s, -30%%); " +
            "-fx-border-width: 0 0 3 0; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;",
            typeColor, typeColor
        );
        btn.setStyle(normalStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(String.format(
            "-fx-background-color: derive(%s, 15%%); " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: derive(%s, -30%%); " +
            "-fx-border-width: 0 0 3 0; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;",
            typeColor, typeColor
        )));

        btn.setOnMouseExited(e -> btn.setStyle(normalStyle));

        return btn;
    }

    /**
     * Crée une carte (VBox) stylisée avec couleur de type et effets hover.
     * Utilisée pour les cartes de monstres, attaques, objets dans les choix de combat.
     *
     * @param typeColor La couleur du type (ex: "#e85858")
     * @param width Largeur de la carte
     * @return VBox stylisée avec effets hover configurables
     */
    public static VBox createTypeCard(String typeColor, int width) {
        VBox card = new VBox(4);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(8, 12, 8, 12));
        card.setPrefWidth(width);

        String normalStyle = String.format(
            "-fx-background-color: %s; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: derive(%s, -30%%); " +
            "-fx-border-width: 0 0 3 0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;",
            typeColor, typeColor
        );
        card.setStyle(normalStyle);

        // Ajouter les effets hover
        card.setOnMouseEntered(e -> card.setStyle(String.format(
            "-fx-background-color: derive(%s, 15%%); " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: derive(%s, -30%%); " +
            "-fx-border-width: 0 0 3 0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;",
            typeColor, typeColor
        )));

        card.setOnMouseExited(e -> card.setStyle(normalStyle));

        return card;
    }

    /**
     * Ajoute des effets hover simples à n'importe quel Node.
     * Éclaircit l'élément au survol.
     *
     * @param node Le node à animer
     * @param normalStyle Le style normal
     * @param hoverStyle Le style au survol
     */
    public static void addHoverEffect(Node node, String normalStyle, String hoverStyle) {
        node.setStyle(normalStyle);
        node.setOnMouseEntered(e -> node.setStyle(hoverStyle));
        node.setOnMouseExited(e -> node.setStyle(normalStyle));
    }

    /**
     * Ajoute des effets hover avec animation de scale (agrandissement).
     * Utilisé pour les cartes de sélection.
     *
     * @param node Le node à animer
     * @param scaleFrom Taille de départ (généralement 1.0)
     * @param scaleTo Taille au survol (généralement 1.05)
     */
    public static void addScaleHoverEffect(Node node, double scaleFrom, double scaleTo) {
        node.setOnMouseEntered(e -> {
            node.setScaleX(scaleTo);
            node.setScaleY(scaleTo);
        });
        node.setOnMouseExited(e -> {
            node.setScaleX(scaleFrom);
            node.setScaleY(scaleFrom);
        });
    }

    /**
     * Génère un style CSS pour un bouton avec bordure 3D.
     *
     * @param bgColor Couleur de fond
     * @param borderColor Couleur de bordure
     * @param borderWidth Largeur de bordure (format: "0 0 3 0")
     * @param radius Rayon des coins
     * @return String de style CSS
     */
    public static String createButtonStyle(String bgColor, String borderColor, String borderWidth, int radius) {
        return String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: %d; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: %s; " +
            "-fx-border-radius: %d; " +
            "-fx-cursor: hand;",
            bgColor, radius, borderColor, borderWidth, radius
        );
    }

    /**
     * Génère un style CSS pour une carte avec bordure 3D.
     *
     * @param bgColor Couleur de fond (ou gradient)
     * @param borderColor Couleur de bordure
     * @param borderWidth Largeur de bordure (format: "0 0 3 0")
     * @param radius Rayon des coins
     * @return String de style CSS
     */
    public static String createCardStyle(String bgColor, String borderColor, String borderWidth, int radius) {
        return String.format(
            "-fx-background-color: %s; " +
            "-fx-background-radius: %d; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: %s; " +
            "-fx-border-radius: %d; " +
            "-fx-cursor: hand;",
            bgColor, radius, borderColor, borderWidth, radius
        );
    }

    /**
     * Dérive une couleur (éclaircit ou assombrit).
     * Retourne la string CSS "derive(color, percentage%)".
     *
     * @param color Couleur de base
     * @param percentage Pourcentage (-100 à 100)
     * @return String CSS derive
     */
    public static String deriveColor(String color, int percentage) {
        return String.format("derive(%s, %d%%)", color, percentage);
    }

    /**
     * Formate l'affichage des points de vie.
     *
     * @param current PV actuels
     * @param max PV maximum
     * @return String formatée (ex: "100 / 150 PV")
     */
    public static String formatHpText(double current, double max) {
        return String.format("%.0f / %.0f PV", current, max);
    }

    /**
     * Crée un Label stylisé avec police personnalisée.
     *
     * @param text Le texte du label
     * @param fontSize Taille de la police
     * @param weight Poids de la police (NORMAL, BOLD, etc.)
     * @param color Couleur du texte
     * @return Label stylisé
     */
    public static Label createLabel(String text, int fontSize, FontWeight weight, Color color) {
        Label label = new Label(text);
        label.setFont(Font.font(FontConfig.SYSTEM.getFontName(), weight, fontSize));
        label.setTextFill(color);
        return label;
    }

    /**
     * Détermine la couleur de la barre de HP selon le ratio.
     * - Vert (#48d048) si ratio > 50%
     * - Jaune (#f8c800) si ratio > 20%
     * - Rouge (#f85858) sinon
     *
     * @param ratio Ratio HP (current / max), entre 0.0 et 1.0
     * @return Couleur hex
     */
    public static String getHpColor(double ratio) {
        if (ratio > 0.5) {
            return ColorConfig.HP_COLOR_HIGH.getColorCode();
        } else if (ratio > 0.2) {
            return ColorConfig.HP_COLOR_MEDIUM.getColorCode();
        } else {
            return ColorConfig.HP_COLOR_LOW.getColorCode();
        }
    }

    /**
     * Crée un VBox pour le contenu d'un bouton (deux lignes de texte).
     * Utilisé pour les boutons d'attaque avec nom + infos.
     *
     * @param titleText Texte du titre (ligne 1, gras, taille 12)
     * @param infoText Texte des infos (ligne 2, normal, taille 9)
     * @param disabled Si true, utilise des couleurs grisées
     * @return VBox contenant les deux labels
     */
    public static VBox createButtonContent(String titleText, String infoText, boolean disabled) {
        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(0, 8, 0, 8));

        Color titleColor = disabled ? Color.GRAY : Color.WHITE;
        Color infoColor = disabled ? Color.DARKGRAY : Color.web("#ddd");

        Label titleLabel = createLabel(titleText, 12, FontWeight.BOLD, titleColor);
        Label infoLabel = createLabel(infoText, 9, FontWeight.NORMAL, infoColor);

        content.getChildren().addAll(titleLabel, infoLabel);
        return content;
    }

    /**
     * Détermine la couleur d'un objet selon son nom.
     * Utilise des mots-clés pour identifier le type d'objet.
     *
     * @param itemName Nom de l'objet
     * @return Couleur hex correspondante
     */
    public static String getItemColor(String itemName) {
        String lowerName = itemName.toLowerCase();

        if (lowerName.contains("medicament") || lowerName.contains("anti")) {
            return ColorConfig.ITEM_MEDICINE_COLOR.getColorCode();
        } else if (lowerName.contains("degat")) {
            return ColorConfig.ITEM_POTION_DEGATS_COLOR.getColorCode();
        } else if (lowerName.contains("vitesse")) {
            return ColorConfig.ITEM_POTION_VITESSE_COLOR.getColorCode();
        } else {
            return ColorConfig.ITEM_DEFAULT_COLOR.getColorCode();
        }
    }
}
