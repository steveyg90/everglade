/*

You will soon have a game, now you need the product - and they are substantially different.

The product is the thing that can be marketed.

Marketing means, you can talk about it in a way that makes for good storytelling.
For many goods, and hugely popular ones, it's a simple story that just needs to be hammered home over and over:
"the burger is tasty, it will melt in my mouth. I'll ascend to heaven with this burger."
And then the story gets more detailed as necessary - it's "exotic and tangy", it's "healthy and sustainable",
and so on to differentiate by audience demographics. When you have a story, people involve themselves in that story,
and the product becomes a necessary part of it.

Games are in the same sort of position, but without quite so obvious selling points:
they have to say something to someone, in a way that you can pitch quickly.
And this means that the marketing of the game is built into the game design
- it can't just be a list of software features or assets,
because that isn't something people can anticipate as an experience.
What tends to be asked when players can't anticipate is: "what do you do in this game?"
And it can always be a simple answer, like "it's about the feeling of running and jumping"
or "it's about stacking blocks", but whatever it is, the game has to deliver on really being about that claim,
and can't drift off into incoherence.

If you have both the game and the marketing story,
then you will have a starting point for making marketing materials and getting attention on it.
For that there are all kinds of venues - you can just post the game to itch,
you can go on social media and hashtag it, you can try talking to journalists and influencers.
You don't want to be in the position of just going "ok, here I am, world",
because then the only narrative you fit into is "naive newcomer".
But if you have a crackling hot story,
everyone will look for a way to insert themselves in that narrative.

If nobody can tell the story of the game, not even yourself, it can't become a viable product
- though sometimes such a story emerges by sheer chance, like that of Flappy Bird.

 */

package com.sdgja.intohell;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.*;
import com.sdgja.GameSprite.apparel.Hat;
import com.sdgja.GameSprite.weapon.Weapon;
import com.sdgja.SAJInventory.InventoryManager;
import com.sdgja.SAJInventory.WindowManager;
import com.sdgja.Ui.MouseInterface;
import com.sdgja.actor.Actor;
import com.sdgja.actor.Player;
import com.sdgja.background.BackgroundSwapper;
import com.sdgja.camera.Camera;
import com.sdgja.entity.sprite.*;
import com.sdgja.entity.structure.EntityManager;
import com.sdgja.filesystem.IFileName;
import com.sdgja.musicPlay;
import com.sdgja.hud.health.HealthPower;
import com.sdgja.map.IMap;
import com.sdgja.map.MapGeneration;
import com.sdgja.parallax.ParallaxBackground;
import com.sdgja.parallax.ParallaxLayer;
import com.sdgja.raycast.Ambient;
import com.sdgja.raycast.RayCast;
import com.sdgja.screen.AbstractScreen;
import com.sdgja.screen.CharacterCreation;
import com.sdgja.screen.ScreenEnum;
import com.sdgja.screen.ScreenManager;
import com.sdgja.tmx.TmxRenderer;
import com.sdgja.utils.*;
import com.sdgja.weather.NightCycle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.sdgja.filesystem.IFileName.FNGuiTexture;
import static com.sdgja.musicPlay.nextMusicTrack;
import static com.sdgja.musicPlay.prevMusicTrack;
import static com.sdgja.utils.DebugShell.getSpawnString;

public class IntoHellGame extends AbstractScreen  {

	private boolean quit = false;
	private String OSstring = "OS: ";
	private ShapeRenderer shapeRenderer;
	private int fadingBiome =0;
	private float [] fadeAlpha = {	0.0f,0.3f,0.525f,0.650f,0.750f,0.825f,0.875f,0.9f,0.92f,0.94f,0.96f,0.98f,1f,
			0.8f,0.6f,0.4f,0.2f};
	private BackgroundSwapper backgroundSwapper;
	private SpriteBatch batch;
	private static SpriteBatch lightbatch;
	private MapGeneration map;
	public static Camera camera;
	public static Viewport gamePort;

	private float oldCameraX;
	private float oldCameraY;
	private static float cameraDifferenceX;
	private float cameraDifferenceY;


//	private ExtendViewport viewport;

	private static FrameBuffer lightBuffer;
	private TextureRegion lightBufferRegion;
	private OrthographicCamera frameBufferCamera;

	private Texture coalTexture;
	public static Texture[] skyTexture = new Texture[4];

	private Texture[] frontTexture = new Texture[4];
	private Texture[] midTexture = new Texture[4];
	private Texture[] backTexture = new Texture[4];

	private static Texture lightTexture;

	// TODO: This value needs to be properly calculated dependent on screen resolution
	private float ZOOM = IMap.ZOOM;;
	// TODO: These values need to be properly calculated dependent on screen resolution

	public static ParallaxBackground[] cloudBackground=new ParallaxBackground[4];

	// Our player
	private static Actor player, hat, sword;

	public static Actor getPlayer() { return player;}   // allow player to be accessed from any other class

	public static Texture getSkyTexture() { return skyTexture[0]; }

	public static float getCameraDiffernceX() { return cameraDifferenceX;}

//	public static ShaderProgram nightShader, backgroundShader, outlineShader, basicShader, lighterNightShader;

	public static float nightCol = 1.0f, nightCol2 = 1.0f;

	private boolean showDebugPanelOne = false, showStructureDebug = false;

	public static  boolean bInventoryShowing = true, bQuestsShowing = true, bConsoleShowing = true, bInputWindow = true;

	private static WindowManager winManager = null;

	private InputMultiplexer inputMultiplexer = new InputMultiplexer();

	private int keyPressed = 0;

	private NightCycle sunCycle,moonCycle;

	public static int biome = 0;


	//////////////////////////////////////////////////////////////////////////////////

	public static FrameBuffer weatherBuffer;
	public static  TextureRegion weatherRegion;
	public static SpriteBatch weatherBatch;

	private static Texture guiTexture;

	public IntoHellGame() {
		create();
	}

	@Override
	public void buildStage() {

	}

	public static WindowManager getWinManager() { return winManager;}

	public static Texture getGuiTexture() { return guiTexture;}

	//@Override
	public void create () {

		guiTexture = new Texture(FNGuiTexture);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Create framebuffer for sky and parallax stuff
		weatherBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, 1920, 1080,false);
		weatherBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		weatherRegion = new TextureRegion(weatherBuffer.getColorBufferTexture(),0,0,1920,1080);
		weatherRegion.flip(false, false);
		weatherBatch = new SpriteBatch();

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		winManager = new WindowManager();
		inputMultiplexer.addProcessor(winManager);

		winManager.addWindow("Debug",new InventoryManager("Debug",true, true,7,16,0,685));
		winManager.addWindow("Console",new InventoryManager("Console",true, false,20,6,640,160));

		winManager.addInfo("Console", "Mecha Firma V1.0");
		winManager.addInfo("Console","Date started November 2019");
		winManager.addInfo("Console", "Windows OS Build");
		winManager.addInfo("Console","");
		winManager.addInfo("Console","Release Version " + IMap.VERSION);
		winManager.addInfo("Console","");
		winManager.addInfo("Console", "Terrain Engine " + IMap.TERRAINVERSION);
		winManager.addInfo("Console","");
		winManager.addInfo("Console", "JayCast Light Engine " + IMap.RAYCASTVERSION);
		winManager.addInfo("Console","");
		winManager.addInfo("Console", "UI Window " + IMap.GUIVERSION);
		winManager.addInfo("Console","");
		winManager.addInfo("Console","");
		winManager.addInfo("Console","Written with blood and sweat and many long hours");
		winManager.addInfo("Console","");
		winManager.addInfo("Console","");
		winManager.addInfo("Console","Now go and play the game");

		winManager.addWindow("Input",new InventoryManager("Input",GlobalVar.coreX/64,6,32,32,0,160, inputMultiplexer));
		winManager.addInfo("Input","This is our debug window");

		Gdx.input.setInputProcessor(inputMultiplexer);

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Create debug window
	/*	debugWindow = new DebugWindow();
		debugWindow.buildStage();
		debugWindow.show();
*/
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		switch (GetOS.getOS()) {
			case WINDOWS:
				OSstring = OSstring + "Windows";
				break;
			case MAC:
				OSstring = OSstring + "Mac";
				break;
			case LINUX:
				OSstring = OSstring + "Linux";
				break;
			case SOLARIS:
				OSstring = OSstring + "Solaris";
				break;
			default:
				break;
		};

//		GlobalVar.coreX=Gdx.graphics.getWidth();
//		GlobalVar.coreY=Gdx.graphics.getHeight();
		camera = new Camera(GlobalVar.coreX, GlobalVar.coreY,ZOOM);
		gamePort = new FitViewport(GlobalVar.coreX,GlobalVar.coreY,camera); // Zooms with aspect and borders
		SpriteManager.getInstance().removeAll();

		RayCast.getInstance().clearLightmap();

		XMLReader.loadTileBinary();
		batch = new SpriteBatch();
		lightbatch = new SpriteBatch();
//
//		basicShader = new ShaderProgram(Gdx.files.absolute(IFileName.FNbasicShaderVert),
//				Gdx.files.absolute(IFileName.FNbasicShaderFrag));
//		if(!basicShader.isCompiled()) {
//			System.err.println(basicShader.getLog());
//			System.exit(1);
//		}
//
//		backgroundShader = new ShaderProgram(Gdx.files.absolute(IFileName.FNbackgroundShaderVert),
//				Gdx.files.absolute(IFileName.FNbackgroundShaderFrag));
//		if(!backgroundShader.isCompiled()) {
//			System.err.println(backgroundShader.getLog());
//			System.exit(1);
//		}
//		outlineShader = new ShaderProgram(Gdx.files.absolute(IFileName.FNOutlineShaderVert),
//				Gdx.files.internal(IFileName.FNOutlineShaderFrag));
//		if(!outlineShader.isCompiled()) {
//			System.err.println(outlineShader.getLog());
//			System.exit(1);
//		}


		Vector2 playerPosition = new Vector2((IMap.WIDTH*8)/2,(((IMap.PLATFORM_HEIGHT+IMap.TERRAIN_HEIGHT/2)*8)-64));

		player = new Player(null, IFileName.FNPlayerSprite,"idle", playerPosition, camera);
		hat = new Hat(null, IFileName.FNWizardHat, "idle", playerPosition, camera);
		sword = new Weapon(null,  IFileName.FNWeapon, "idle", playerPosition, camera);

		((Player)player).addApparel(sword, "playerhand","weaponbottom");
		((Player)player).addApparel(hat, "hatconnector","hatbottom");


		player.getSpriter().characterMaps[0]  = CharacterCreation.PlayerCharacterMap.getCharMap("head",PlayerAttributes.head);
		player.getSpriter().characterMaps[1]  = CharacterCreation.PlayerCharacterMap.getCharMap("torso",PlayerAttributes.torso);

		// Vector2 worldPosition, Vector2 dimension, String scml, String animFrame, boolean active
//		SpriteManager.getInstance().addSpriteEntity(new Pengo(new Vector2(player.getPlayerPosition().x-200,player.getPlayerPosition().y), new Vector2(32,32), true,false,true));
//		SpriteManager.getInstance().addSpriteEntity(new Pengo(new Vector2(player.getPlayerPosition().x-400,player.getPlayerPosition().y), new Vector2(32,32), true,false,true));
//		for (int i=0;i<(IMap.WIDTH/8);i++) {
		for (int i=0;(i*200)<((IMap.WIDTH*16));i++) {
			SpriteManager.getInstance().addSpriteEntity(new Pengo(new Vector2((i*200), (IMap.PLATFORM_HEIGHT+IMap.TERRAIN_HEIGHT)*8), new Vector2(32, 32), true, false, false));
			SpriteManager.getInstance().addSpriteEntity(new Caterfly(new Vector2((i*200), (IMap.PLATFORM_HEIGHT+IMap.TERRAIN_HEIGHT)*8), new Vector2(32, 32), true, false, false));
		}
		SpriteManager.getInstance().addSpriteEntity(new SpriteTeleporter(new Vector2(200,400), new Vector2(80,80),true,false,false));
		SpriteManager.getInstance().addSpriteEntity(new SpriteApparel(new Vector2(player.getPlayerPosition().x+200,player.getPlayerPosition().y), new Vector2(32,32),true,false,false));

		SpriteManager.getInstance().sort();

		backgroundSwapper = new BackgroundSwapper(1920,1080,0,Gdx.graphics.getHeight()-1080,IFileName.FNcoalWall );

		ShaderProgram.pedantic = false;
//		nightShader = new ShaderProgram(
//				Gdx.files.absolute(IFileName.ROOT + "/shaders/nightshader.vert"),
//				Gdx.files.absolute(IFileName.ROOT + "/shaders/nightshader.frag"));
//
//		if(!nightShader.isCompiled()) {
//			System.err.println(nightShader.getLog());
//		}
//		lighterNightShader = new ShaderProgram(
//				Gdx.files.absolute(IFileName.ROOT + "/shaders/nightshader.vert"),
//				Gdx.files.absolute(IFileName.ROOT + "/shaders/lighternightshader.frag"));
//
//		if(!lighterNightShader.isCompiled()) {
//			System.err.println(lighterNightShader.getLog());
//		}

		Config.readConfigFile("config.json");  // read our config json file

		TmxRenderer.loadStructures(MapGeneration.getBuildingMap());

		map = MapGeneration.getInstance();
		Vector2 pos;

		TVector<Integer> tpos = new TVector<>(20,30);
		tpos.getX();
		tpos.getY();

		TmxRenderer.drawStructureIntoMap("treasureroom", MapGeneration.getForegroundMap(), 100, IMap.PLATFORM_HEIGHT+IMap.TERRAIN_HEIGHT);
		int x=0;
		while (x<(IMap.WIDTH-27)){
			TmxRenderer.drawStructureIntoMap("cloudstrip", MapGeneration.getForegroundMap(), x, IMap.PLATFORM_HEIGHT-24);
			x+=27;
		}


		skyTexture[3] = 	new Texture(IFileName.ROOT + "/weathergfx/Desert/sky.png");
		backTexture[3] = 	new Texture(IFileName.ROOT + "/weathergfx/Desert/back.png");
		midTexture[3] = 	new Texture(IFileName.ROOT + "/weathergfx/Desert/mid.png");
		frontTexture[3] = 	new Texture(IFileName.ROOT + "/weathergfx/Desert/front.png");

		skyTexture[2] = 	new Texture(IFileName.ROOT + "/weathergfx/Arctic/sky.png");
		backTexture[2] = 	new Texture(IFileName.ROOT + "/weathergfx/Arctic/back.png");
		midTexture[2] = 	new Texture(IFileName.ROOT + "/weathergfx/Arctic/mid.png");
		frontTexture[2] = 	new Texture(IFileName.ROOT + "/weathergfx/Arctic/front.png");

		skyTexture[1] = 	new Texture(IFileName.ROOT + "/weathergfx/Jungle/sky.png");
		backTexture[1] = 	new Texture(IFileName.ROOT + "/weathergfx/Jungle/back.png");
		midTexture[1] = 	new Texture(IFileName.ROOT + "/weathergfx/Jungle/mid.png");
		frontTexture[1] = 	new Texture(IFileName.ROOT + "/weathergfx/Jungle/front.png");

		skyTexture[0] = 	new Texture(IFileName.ROOT + "/weathergfx/Forest/sky.png");
		backTexture[0] = 	new Texture(IFileName.ROOT + "/weathergfx/Forest/back.png");
		midTexture[0] = 	new Texture(IFileName.ROOT + "/weathergfx/Forest/mid.png");
		frontTexture[0] = 	new Texture(IFileName.ROOT + "/weathergfx/Forest/front.png");

		sunCycle = new NightCycle();
		moonCycle = new NightCycle();
		try {
			sunCycle.addImage("sun.png", new Vector2(Gdx.graphics.getWidth()-760,Gdx.graphics.getHeight()/2 - 250),0.f,true);
			moonCycle.addImage("moon0.png", new Vector2(Gdx.graphics.getWidth()-760,Gdx.graphics.getHeight()/2 - 250), 3f,false);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create 4 parallax background objects for the four biomes
		for(int i=0; i<cloudBackground.length;i++) {
			cloudBackground[i] = new ParallaxBackground(new ParallaxLayer[] {
					new ParallaxLayer(backTexture[i], 	new Vector2(3f,0f),	new Vector2(0,0)),
					new ParallaxLayer(midTexture[i],	new Vector2(4f,0f),	new Vector2(0,0)),
					new ParallaxLayer(frontTexture[i],	new Vector2(5f,0f),	new Vector2(0,0)),

			}, 1920, 1080, new Vector2(4f, 0));
		}

		lightTexture = new Texture(IFileName.FNlight);
		coalTexture = new Texture(IFileName.FNcoalWall);

		DisposableMemoryManager.addDisposable(lightTexture);
		DisposableMemoryManager.addDisposable(backTexture[0]);
		DisposableMemoryManager.addDisposable(midTexture[0]);
		DisposableMemoryManager.addDisposable(frontTexture[0]);
		DisposableMemoryManager.addDisposable(coalTexture);
//		DisposableMemoryManager.addDisposable(outlineShader);
//		DisposableMemoryManager.addDisposable(nightShader);
//		DisposableMemoryManager.addDisposable(backgroundShader);

//		spotx = 1220;
//		spoty = 300;

		frameBufferCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		createLightFrameBuffers();
		setInitialCamera();

		biome = Player.getCurrentBiome(getPlayer());

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

		HealthPower.initHUD();
		musicPlay.initMusic();
	}

	private void setInitialCamera(){
		camera.position.x 	= ((player.getPlayerPosition().x) - (Gdx.graphics.getWidth()/2))* ZOOM;
		camera.position.y	= ((player.getPlayerPosition().y) - (Gdx.graphics.getHeight()/2))* ZOOM;
	}

	private void createLightFrameBuffers() {
		if(lightBuffer!=null) {
			lightBuffer.dispose();
		}
		lightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),false);
		lightBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		lightBufferRegion = new TextureRegion(lightBuffer.getColorBufferTexture(),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		lightBufferRegion.flip(false, false);
	}


//  ██████╗ ███████╗███╗   ██╗██████╗ ███████╗██████╗
//  ██╔══██╗██╔════╝████╗  ██║██╔══██╗██╔════╝██╔══██╗
//  ██████╔╝█████╗  ██╔██╗ ██║██║  ██║█████╗  ██████╔╝
//  ██╔══██╗██╔══╝  ██║╚██╗██║██║  ██║██╔══╝  ██╔══██╗
//  ██║  ██║███████╗██║ ╚████║██████╔╝███████╗██║  ██║
//  ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝╚═════╝ ╚══════╝╚═╝  ╚═╝
//



	public void render () {
		if(quit)
			return;
		gamePort.apply();
// Render all sky & biome graphics to frame buffer for scaling to current resolution
		weatherBatch.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ParallaxBackground.setWeatherCamera();
// Render sun moon sky stars
		sunCycle.performCycle(true);
		moonCycle.performCycle(false);
// Parallax
		cloudBackground[biome].render( null, camera.position.y);
		if(!cloudBackground[biome].getCloudStatus()) {
			backgroundSwapper.drawBackground(camera.position.y);
		}
// Draw buffer to screen
		weatherBatch.draw(weatherRegion,0,0);
// Fade solid block in and out for biome change
		if (fadingBiome!=0 && cloudBackground[biome].getRGB()==1.0f){
			float alpha = fadeAlpha[fadingBiome];
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(new Color(0, 0, 0.05f, alpha)); // last argument is alpha channel
			shapeRenderer.rect(0, 0, GlobalVar.coreX, GlobalVar.coreY);
			shapeRenderer.end();
			if ((FrameCount.getFrameCount()&1)==1) {
				fadingBiome--;
			}
			if (fadingBiome==(12)){
				fadingBiome--;
				this.biome = Player.getCurrentBiome(getPlayer());
				NightCycle.setSkyTexture(skyTexture[this.biome]);   // switch sky texture
			}
		} else {
			if (this.biome!=Player.getCurrentBiome(getPlayer())) {
				if (fadingBiome == 0) {
					fadingBiome = fadeAlpha.length-1;
				}
			}
		}
		weatherBatch.end();
// Biome stuff done!

// Clear ambient light buffer
		Ambient.clearAmbientBuffer();
// Clear RayCast lightmap
		RayCast.getInstance().clearLightmap();
// Check if player is placing an object (building/torch etc)
		MouseInterface.checkBuildingPlacement((int)camera.position.x / IMap.TILEWIDTH, (int)camera.position.y / IMap.TILEHEIGHT,ZOOM);
// Update frame count
		FrameCount.updateFrameCount();
// Player logic
		player.poll();
// draw map at these coords
		calculateCamFromPlayer();
// generate raycast from player
		if (!cloudBackground[biome].getCloudStatus()) {
			RayCast.getInstance().rayCastPlayer(player);
		}


		if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
			nextMusicTrack();
		} else {
			if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
				prevMusicTrack();
			}
		}

		if(IMap.DEBUG_MODE) {
					winManager.turnWindowVisible("Input", bInputWindow);
					winManager.turnWindowVisible("Console", bConsoleShowing);
					winManager.turnWindowVisible("Debug", bQuestsShowing);
			keyPressed = 0;
			if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {  // input window
				keyPressed |= 0x8;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {  // input window
				keyPressed |= 0x4;
			}

			if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
				keyPressed |= 0x10;
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {  // input window
				keyPressed |= 0x4;
			}
			if ((keyPressed & 0xc) == 0xc) {
					winManager.turnWindowVisible("Input", bInputWindow);
				keyPressed = 0;
				bInputWindow = !bInputWindow;
			}
			if ((keyPressed & 0x14) == 0x4) {
					winManager.turnWindowVisible("Debug", bQuestsShowing);
				keyPressed = 0;
				bQuestsShowing = !bQuestsShowing;
			}
			if ((keyPressed & 0x14) == 0x14) {
					winManager.turnWindowVisible("Console", bConsoleShowing);
				keyPressed = 0;
				bConsoleShowing = !bConsoleShowing;
			}
		}
		/*if(MouseFocus.getFocus()==MouseFocus.WindowFocus.Gui ||
				MouseFocus.getFocus()==MouseFocus.WindowFocus.Main) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {  // inventory
				winManager.turnWindowVisible("Inventory", bInventoryShowing);
				bInventoryShowing = !bInventoryShowing;
			}
		}*/


		if(MouseFocus.getFocus()!=MouseFocus.WindowFocus.InputWindow) {
			DebugShell.processDebugKeyCheck();
			if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {  // probably need clean up here
				ScreenManager.getInstance().showScreen(IMap.bMenuScreens ? ScreenEnum.TITLE : ScreenEnum.GAME);
				quit = true;
			}
		}
			batch.setProjectionMatrix(camera.combined); // bind to main camera
			batch.setShader(null);
			if(DebugShell.renderBackMap()) {
				MapGeneration.renderFoliageMap(batch, (int)camera.position.x / IMap.TILEWIDTH, (int)camera.position.y / IMap.TILEHEIGHT);
				batch.begin();
				MapGeneration.renderBackMap(batch, (int)camera.position.x / IMap.TILEWIDTH, (int)camera.position.y / IMap.TILEHEIGHT);
				batch.end();
			}

			EntityManager.playAll();
			batch.begin();
			if(DebugShell.renderBuilding())
				MapGeneration.renderBuildingMap(batch, (int)camera.position.x / IMap.TILEWIDTH, (int)camera.position.y / IMap.TILEHEIGHT);
			batch.end();

			float rgb=backgroundSwapper.getRGB();
			if(!cloudBackground[biome].getCloudStatus()) {
				if (!Config.getLightEnable()) {
					Ambient.drawPlayerAmbient(player.getPlayerPosition().x,player.getPlayerPosition().y);
					Matrix4 uiMatrix = frameBufferCamera.combined;
					uiMatrix.setToOrtho2D(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
					frameBufferCamera.setToOrtho(true);
					batch.setProjectionMatrix(uiMatrix);
					Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR,GL20.GL_ZERO);
					batch.begin();
					batch.setColor(.3f,.3f,.3f,rgb/1.25f);
					batch.draw(lightBufferRegion,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
					batch.end();
				}
			}
			ViewableSpriteEntity.batch.begin();
			SpriteManager.getInstance().playAll();
			ViewableSpriteEntity.batch.end();

			if(DebugShell.renderSprites()){
// Sprites - all these use own batch handled in the Actor class
			}
			player.draw();
			hat.draw();
			sword.draw();
//			player.drawRectangle();  // TODO: SDG may use for debug


//		if(DebugShell.renderForeground()) {

		batch.begin();
		batch.setProjectionMatrix(camera.combined); // bind to main camera
		if(DebugShell.renderForeground()) {
			 rgb=backgroundSwapper.getRGB();
// Foreground Map
			MapGeneration.renderMap(batch, (int)camera.position.x / IMap.TILEWIDTH, (int)camera.position.y / IMap.TILEHEIGHT, !cloudBackground[biome].getCloudStatus(),rgb);
		}
		int index = ((FrameCount.getFrameCount()>>3)&15);
		float alpha = resourcePulses[index];
		batch.setColor(alpha,alpha,alpha,0.75f+(alpha/2f));
// Resource Map
		MapGeneration.renderResourceMap(batch, (int)camera.position.x / IMap.TILEWIDTH, (int)camera.position.y / IMap.TILEHEIGHT);
		float pulse =(((FrameCount.getFrameCount())>>2)&3);
		pulse = (pulse/10)+0.7f;
		if (MouseInterface.isBuildingValid()){
			batch.setColor(pulse/2, pulse, pulse/2, 1.5f);
		} else {
			batch.setColor(pulse, 0, 0, 1.5f);
		}
		if(DebugShell.renderBuilding())
// Building Place Map
			MapGeneration.renderBuildingPlaceMap(batch, (int)camera.position.x / IMap.TILEWIDTH, (int)camera.position.y / IMap.TILEHEIGHT);
		batch.setColor(1f, 1f, 1f, 1f);
		batch.end();

		HealthPower.drawHUD();
	/*	String strCamPos = "";
		strCamPos = String.format("Camera x,y:(%.0f,%.0f)", camera.position.x, camera.position.y);

		debugWindow.addInfo(OSstring);
		debugWindow.addInfo(strCamPos);
		String strPlayerPos = String.format("Player x,y:(%.0f,%.0f)", player.getPlayerPosition().x, player.getPlayerPosition().y);
		debugWindow.addInfo(strPlayerPos);
		debugWindow.addInfo("1 Background : " 	+ (DebugShell.renderBackMap() 		? "Active" : "off"));
		debugWindow.addInfo("2 Building...: " 	+ (DebugShell.renderBuilding() 		? "Active" : "off"));
		debugWindow.addInfo("3 Foreground.: " 	+ (DebugShell.renderForeground() 	? "Active" : "off"));
		debugWindow.addInfo("4 Sprites....: " 	+ (DebugShell.renderSprites() 		? "Active" : "off"));
		debugWindow.addInfo("5 Foilage....: " 	+ (DebugShell.renderBackMap() 		? "Active" : "off"));
		debugWindow.addInfo("Spawn String:" + getSpawnString());
		debugWindow.addInfo("Day number...: " + NightCycle.getDay());
		debugWindow.addInfo("Time.........: " + NightCycle.getTime());
		debugWindow.addInfo("Moon Cycle...: " + (NightCycle.getDay()&7));
		debugWindow.addInfo("Current Biome: " + Player.getCurrentBiome(getPlayer()));
		debugWindow.addInfo("Core XY : " + GlobalVar.coreX + ", " + GlobalVar.coreY);
		debugWindow.addInfo("Viewport: " + gamePort.getScreenWidth() + ", " + gamePort.getScreenHeight());
		debugWindow.addInfo("Audio Track: " + String.valueOf(musicPlay.getMusicName()));
		String cull = String.format("Total: %d Culled: %d",totalSprites,culled);
		debugWindow.addInfo(cull);

		HashMap<String, Integer> entityMap = EntityManager.getEntityMap();
		for (Map.Entry mapElement : entityMap.entrySet()) {
			String key = (String) mapElement.getKey();
			int amount = (Integer) mapElement.getValue();
			String strInfo = String.format("%d x %s", amount, key);
			debugWindow.addInfo(strInfo);
		}

		if(bQuestsShowing) {
			debugWindow.setVisible(true);
			debugWindow.render(Gdx.input.getDeltaX());
		}*/

		String strCamPos = "";


		strCamPos = String.format("(%.0f,%.0f) Camera x,y", camera.position.x, camera.position.y);
		String strPlayerPos = String.format("(%.0f,%.0f) Player x,y",  player.getPlayerPosition().x, player.getPlayerPosition().y);
		winManager.addInfo("Debug",OSstring);
		winManager.addInfo("Debug","Core XY : " + GlobalVar.coreX + ", " + GlobalVar.coreY);
		winManager.addInfo("Debug","Viewport: " + gamePort.getScreenWidth() + ", " + gamePort.getScreenHeight());

		winManager.addInfo("Debug",strCamPos);
		winManager.addInfo("Debug",strPlayerPos);
		winManager.addInfo("Debug","");
		winManager.addInfo("Debug","1 Background : " 	+ (DebugShell.renderBackMap() 		? "Active" : "off"));
		winManager.addInfo("Debug","2 Building...: " 	+ (DebugShell.renderBuilding() 		? "Active" : "off"));
		winManager.addInfo("Debug","3 Foreground.: " 	+ (DebugShell.renderForeground() 	? "Active" : "off"));
		winManager.addInfo("Debug","4 Sprites....: " 	+ (DebugShell.renderSprites() 		? "Active" : "off"));
		winManager.addInfo("Debug","5 Foilage....: " 	+ (DebugShell.renderBackMap() 		? "Active" : "off"));
		winManager.addInfo("Debug","");
		winManager.addInfo("Debug","Spawn String:" + getSpawnString());
		winManager.addInfo("Debug","");
		winManager.addInfo("Debug","Day number...: " + NightCycle.getDay());
		winManager.addInfo("Debug","Time.........: " + NightCycle.getTime());
		winManager.addInfo("Debug","Moon Cycle...: " + (NightCycle.getDay()&7));
		winManager.addInfo("Debug","Current Biome: " + Player.getCurrentBiome(getPlayer()));
		winManager.addInfo("Debug","");
		winManager.addInfo("Debug","A Game by Steve Green");
		winManager.addInfo("Debug", "and Jay Aldred");

		// Add ti entities window
		HashMap<String, Integer> entityMap = EntityManager.getEntityMap();
		for (Map.Entry mapElement : entityMap.entrySet()) {
			String key = (String) mapElement.getKey();
			int amount = (Integer) mapElement.getValue();
			String strInfo = String.format("%d x %s", amount, key);
			winManager.addInfo("Debug",strInfo);
		}

		winManager.showWindows();

	}

	public static int culled = 0;
	public static int totalSprites = 0;

	private static float resourcePulses [] = {0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,1.0f,1.2f,1.0f,0.9f,0.8f,0.7f,0.6f,0.5f,0.4f};

	private void calculateCamFromPlayer() {
		float cameraX = ((player.getPlayerPosition().x) - camera.viewportWidth/2)* ZOOM;
		float cameraY = ((player.getPlayerPosition().y) - camera.viewportHeight/2)* ZOOM;
		if(cameraX<8)
			cameraX=8;
		if(cameraX>=(IMap.WIDTH-((camera.viewportWidth/16)+1))*8)
			cameraX=(IMap.WIDTH-((camera.viewportWidth/16)+1))*8;
		if(cameraY<8)
			cameraY=8;
		if(cameraY>=(IMap.HEIGHT-((camera.viewportHeight/16)+3))*8)
			cameraY=(IMap.HEIGHT-((camera.viewportHeight/16)+3))*8;
		oldCameraX=camera.position.x;
		oldCameraY=camera.position.y;
		camera.position.x=myLerp(camera.position.x,cameraX,0.05f);
		camera.position.y=myLerp(camera.position.y,cameraY,0.03f);
		cameraDifferenceX=camera.position.x-oldCameraX;
		cameraDifferenceY=camera.position.y-oldCameraY;
		camera.update();
	}

	public static float myLerp(float point1, float point2, float alpha)
	{
		return point1 + alpha * (point2 - point1);
	}

//	// Display building structure amounts
//	private void displayEntities() {
//		HashMap<String, Integer> entityMap = EntityManager.getEntityMap();
//		int x = 340;
//		int y = 120;
//		for (Map.Entry mapElement : entityMap.entrySet()) {
//			String key = (String)mapElement.getKey();
//			int amount = (Integer)mapElement.getValue();
//			String strInfo = String.format("%d x %s", amount, key);
////			hudCamera.drawText(x, y, strInfo);
//			if(y >240) {
//				y=120;
//				x=440;
//			} else {
//				y+=20;
//			}
//		}
//	}

	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height){
		gamePort.update(width,height);
		camera.update();
	}


	@Override
	public void render(float delta) {
		this.render();
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		//batch.dispose();

		TmxRenderer.removeAll();
		DisposableMemoryManager.cleanUp();
		EntityManager.removeAll();
		MapGeneration.dispose();
		weatherBuffer.dispose();
		lightBuffer.dispose();
		//	super.dispose();

	}

	public static FrameBuffer getLightBuffer () {return lightBuffer;}
	public static SpriteBatch getLightbatch () {return lightbatch;}
	public static Texture getLightTexture () {return lightTexture;}

}
