package com.sdgja.filesystem;

public interface IFileName {
    // Parent folder
    String ROOT = "./core/assets/";

    // Child folders
    String SCMLroot     = ROOT + "scml/";
    String SHADEroot    = ROOT + "shaders/";
    String WEATHERroot  = ROOT + "weathergfx/";
    String LIGHTroot    = ROOT + "lights/";
    String HUDroot      = ROOT + "Hud/";
    String TILEroot     = ROOT + "Tiles/";
    String SOUNDroot    = ROOT + "sounds/";

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Background graphics
    String FNcoalWall   = WEATHERroot + "coalwall.png";

    // Lights
    String FNlight      = LIGHTroot + "light.png";

    // Shaders
    String FNbasicShaderVert        = SHADEroot + "basicShader.vert";
    String FNbasicShaderFrag        = SHADEroot + "basicShader.frag";
    String FNbackgroundShaderVert   = SHADEroot + "backgroundShader.vert";
    String FNbackgroundShaderFrag   = SHADEroot + "backgroundShader.frag";
    String FNOutlineShaderVert      = SHADEroot + "OutlineShader.vert";
    String FNOutlineShaderFrag      = SHADEroot + "OutlineShader.frag";

    // SCML
    String FNPlayerSprite   = SCMLroot + "PlayerSprite/character.scml";
    String FNWizardHat      = SCMLroot + "Apparel/hats/german/german.scml";         // "Apparel/hats/wizardone/wizard.scml";
    String FNIndianaHat     = SCMLroot + "apparel/hats/indiana/indiana.scml";
    String FNWeapon         = SCMLroot + "weapon/axe/axe.scml";                     //  "weapon/weapon.scml";
    String FNPengo          = SCMLroot + "pengo/pengo.scml";
    String FNCaterfly       = SCMLroot + "greyguy3/greyguy.scml"; //"caterfly/caterfly.scml";

    // HUD
    String FNHealth         = HUDroot   + "health.png";
    String FNPower          = HUDroot   + "power.png";
    String FNHotbar         = HUDroot   + "hotbar.png";
    String FNFont           = HUDroot   + "8x16 font.png";
    String FNGuiTexture     = HUDroot   + "GUI.png";
    String FNLeftArrow      = HUDroot   + "leftArrow.png";
    String FNRightArrow     = HUDroot   + "rightArrow.png";
    String FNOkButton       = HUDroot   + "okButton.png";
    String FNCancelButton   = HUDroot   + "cancelButton.png";
    String FNMousePointer   = HUDroot   + "mousePointerGREEN.png";
    String FNCompass        = HUDroot   + "compass.png";
    String FNNeedle         = HUDroot   + "needle.png";
    String FNJournal        = HUDroot   + "journal.png";
    String FNConversation   = HUDroot   + "conversation.png";

    //Tiles
    String FNTileGraphics   = TILEroot  + "tiles.png";
    String FNTileAttributes = TILEroot  + "tiles.bin";

    //Sounds
    String FNMusic0        = SOUNDroot  + "PunchDeck/0-Punch Deck - Ascent to the Peak.mp3";
    String FNMusic1        = SOUNDroot  + "PunchDeck/1-Punch Deck - Bhangra Bass.mp3";
    String FNMusic2        = SOUNDroot  + "PunchDeck/2-Punch Deck - By Force.mp3";
    String FNMusic3        = SOUNDroot  + "PunchDeck/3-Punch Deck - Impatience.mp3";
    String FNMusic4        = SOUNDroot  + "PunchDeck/4-Punch Deck - Omni.mp3";
    String FNMusic5        = SOUNDroot  + "PunchDeck/5-Punch Deck - Oppressive Ambiance.mp3";
    String FNMusic6        = SOUNDroot  + "PunchDeck/6-Punch Deck - Remnant of a Star.mp3";
    String FNMusic7        = SOUNDroot  + "PunchDeck/7-Punch Deck - Shimmering Lights.mp3";
    String FNMusic8        = SOUNDroot  + "PunchDeck/8-Punch Deck - Snowfall.mp3";
    String FNMusic9        = SOUNDroot  + "PunchDeck/9-Punch Deck - The Traveler.mp3";
    String FNMusic10       = SOUNDroot  + "PunchDeck/10-Punch Deck - Wandering the Path.mp3";
    String FNMusic11       = SOUNDroot  + "PunchDeck/11-Punch Deck - What Is and What Could Be.mp3";

}