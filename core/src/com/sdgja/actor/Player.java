////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  ██████╗ ██╗      █████╗ ██╗   ██╗███████╗██████╗
//  ██╔══██╗██║     ██╔══██╗╚██╗ ██╔╝██╔════╝██╔══██╗
//  ██████╔╝██║     ███████║ ╚████╔╝ █████╗  ██████╔╝
//  ██╔═══╝ ██║     ██╔══██║  ╚██╔╝  ██╔══╝  ██╔══██╗
//  ██║     ███████╗██║  ██║   ██║   ███████╗██║  ██║
//  ╚═╝     ╚══════╝╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
// Main player file, controls, background collision etc..
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.sdgja.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.Timeline;
import com.sdgja.camera.Camera;
import com.sdgja.intohell.IntoHellGame;
import com.sdgja.map.IMap;
import com.sdgja.map.MapGeneration;

import static com.sdgja.utils.XMLReader.getTileId;
import com.brashmonkey.spriter.Player.Attachment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.sdgja.utils.GlobalVar;
import com.sdgja.utils.MouseFocus;
import com.brashmonkey.spriter.Entity.CharacterMap;


public class Player extends Actor {


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// ██████╗██╗  ██╗███████╗ █████╗ ████████╗
//██╔════╝██║  ██║██╔════╝██╔══██╗╚══██╔══╝
//██║     ███████║█████╗  ███████║   ██║
//██║     ██╔══██║██╔══╝  ██╔══██║   ██║
//╚██████╗██║  ██║███████╗██║  ██║   ██║
// ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝   ╚═╝

	private final boolean	playerFloating	= true;		// false = normal player control
	private final float		cheatSpeed		= 16f/1.1f;		// Horizontal Movement speed when floating (cheating!)
	private final float		vertCheatSpeed	= 9f/1.1f;		// Vertical

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final float stepUpValue = 15; // how much to move the player up when he hits a single block

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// All player movements are keyed from this speed.
	final static float playerBaseSpeed = 5.5f/1.5f;

	// Multipliers for cheat and non cheat modes
	final static float normalMultiplier = 1.0f;
	final static float cheatMultiplier = 2.5f;

	// Variable for current multiplier (used for cheating!)
	private static float speedMultiplier = 1;

	// Variable for fall multiplier
	private static float playerFallSpeed = 1;
	private final float playerFallStartSpeed = 0.10f;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Bits set for player input
	private final int bitSpeedCheat = 1;
	private final int bitFireButton = 2;
	private final int bitUp = 4;
	private final int bitDown = 8;
	private final int bitLeft = 16;
	private final int bitRight = 32;
	// Variable that stores above bits
	private int playerInput = 0; //buildInput();
	private int oldPlayerInput = 0; //buildInput();

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Possible variations for player status
	private boolean doubleJumpEnabled = true;
	private boolean doubleJumped = false;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Possible variations for player status
	enum p_Actions {
		p_Idle,
		p_Jump,
		p_Fall,
		p_WalkLeft,
		p_WalkRight
	}

	// Current player status
	private p_Actions playerStatus = p_Actions.p_Idle;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Arc of motion for jumping
	int[] playerJumpTable = {15, 12, 11, 10, 9, 8, 7, 7, 6, 6, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1};

	private int playerJumpIndex;

// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean playerFacingRight = true;

	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Character mappings
	private Entity entity = data.getEntity(0);
	final Entity.CharacterMap[] charMaps = {
			entity.getCharacterMap("head0"),
			entity.getCharacterMap("head1"),
			entity.getCharacterMap("head2"),
			entity.getCharacterMap("head3"),

	};
	public int getHeadAmount() { return charMaps.length; }
	public void setHead(int index) {

	//	getPlayer().getEntity().getCharacterMap( "head0");
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Player(ShaderProgram shader, String scml, String animFrame, Vector2 position, Camera camera) {
		super(shader, scml, animFrame, position, camera);
		this.attachments = new ArrayList<>();

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
	private Attachment attach;
	private float xoffset = 0, yoffset=0;
	private Actor hat;

	private List<Actor> attachments;

	// who is the sprite we want to attach to us
	// bone is the bone from where we want to attach from
	// playerDest is point on player to attach to
	public void addApparel(Actor who, String playerDest, String connectorPoint){
		this.attachments.add(who); // add attachment as a spriter player object

		// who is the sprite we want to attach to us
		Timeline.Key.Bone b = who.spriter.getBone(connectorPoint);
		who.xOffset = b.position.x;
		who.yOffset = b.position.y;
		//xoffset = b.position.x;
		//yoffset = b.position.y;
		attach = new Attachment(who.spriter.getBone(connectorPoint)){//} who.spriter.getObject(connectorPoint)) {

			@Override
			protected void setScale(float xscale, float yscale) {
				who.spriter.setScale( Math.max(xscale, yscale));
			}

			@Override
			protected void setPosition(float x, float y) {

				who.spriter.setPosition(x-who.xOffset,  y-who.yOffset);
			}

			@Override
			protected void setAngle(float angle) {
				who.spriter.setAngle(angle);
			}
		};
		attach.setParent(spriter.getBone(playerDest));// spriter.getBone(playerDest));   // players head is bone_003
		spriter.attachments.add(attach);
	}



	//////////////////////////////////////////////////////////////////////////////////////////////
	private void flipAttachments() {
		attachments.forEach(p->p.spriter.flipX());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Main player movement code
	public void poll() {

		playerInput = buildInput();
///////////////////////////////////////////////////////////
// ██████╗██╗  ██╗███████╗ █████╗ ████████╗
//██╔════╝██║  ██║██╔════╝██╔══██╗╚══██╔══╝
//██║     ███████║█████╗  ███████║   ██║
//██║     ██╔══██║██╔══╝  ██╔══██║   ██║
//╚██████╗██║  ██║███████╗██║  ██║   ██║
// ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝   ╚═╝
///////////////////////////////////////////////////////////
		if(IMap.DEBUG_MODE) {

			if (playerFloating && (playerInput & bitSpeedCheat) != 0) {
				if ((playerInput & bitUp) != 0) {
					position.y -= vertCheatSpeed;
				} else {
					if ((playerInput & bitDown) != 0) {
						position.y += vertCheatSpeed/2;
					}
				}
				if ((playerInput & bitLeft) != 0) {
					position.x -= cheatSpeed;
				} else {
					if ((playerInput & bitRight) != 0) {
						position.x += cheatSpeed;
					}
				}
				clampPlayerToMap();
				return;
			}
		}
// End cheat code
///////////////////////////////////////////////////////////
		if ((playerInput & bitDown) != 0) {
			testErase(position.x-8,position.y-8);
		}

		switch (playerStatus) {
			case p_Idle:
				spriter.setAnimation("idle");
				clampYToFloor();
				if ((playerInput & bitLeft) != 0) {
					playerStatus = p_Actions.p_WalkLeft;
				}
				if ((playerInput & bitRight) != 0) {
					playerStatus = p_Actions.p_WalkRight;
				}
				checkForFall();
				checkForJump();
				break;
			case p_WalkLeft:
				spriter.setAnimation("walk");
				clampYToFloor();
				if ((playerInput & bitLeft) == 0) {
					playerStatus = p_Actions.p_Idle;
				} else {
					movePlayerLeft();

				}
				checkForFall();
				if (playerStatus == p_Actions.p_Fall){ 		// Changed from walk into fall
					if (!checkTiles(position.x-playerBaseSpeed, position.y + 26)) {
						position.y +=13;
						clampYToFloor();
						playerStatus = p_Actions.p_WalkLeft;
					}
				}
				checkForJump();
				break;
			case p_WalkRight:
				spriter.setAnimation("walk");
				clampYToFloor();
				if ((playerInput & bitRight) == 0) {
					playerStatus = p_Actions.p_Idle;
				} else {

					movePlayerRight();
				}
				checkForFall();
				if (playerStatus == p_Actions.p_Fall){ 		// Changed from walk into fall
					if (!checkTiles(position.x+playerBaseSpeed, position.y + 26)) {
						position.y +=13;
						clampYToFloor();
						playerStatus = p_Actions.p_WalkRight;
					}
				}
				checkForJump();
				break;
			case p_Jump:
				spriter.setAnimation("jump_peak");
				if (playerJumpIndex == playerJumpTable.length || (playerInput & bitUp) == 0 || (playerBumpedHead() == true)) {
					startFall();
				} else {
					movePlayerHorizontal();
					position.y -= playerJumpTable[playerJumpIndex];
					playerJumpIndex++;
				}
				break;
			case p_Fall:
				if (doubleJumpEnabled == true && doubleJumped == false) {
					checkForJump();
					if (playerStatus == p_Actions.p_Jump) {
						doubleJumped = true;
					}
				}
				spriter.setAnimation("jump_fall");
				if (playerFallSpeed < playerBaseSpeed/2) {
					playerFallSpeed += 0.50;
				}
				if (playerCheckFloor() == true) {
					clampYToFloor();
					position.y-=7;
					playerStatus = p_Actions.p_Idle;
				} else {
					movePlayerHorizontal();
					position.y += playerBaseSpeed + playerFallSpeed;
				}
				break;
			default:
		}
		clampPlayerToMap();
	}



	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void update(Camera camera) {
		float X = ((position.x) - (camera.position.x)/ IMap.ZOOM);
		float Y= (GlobalVar.coreY -((position.y-GlobalVar.spriteYAdjust) - (camera.position.y)/ IMap.ZOOM))-30;
		spriter.setPosition(X,Y); //(((position.x* IMap.ZOOM) - (camera.position.x*IMap.ZOOM)), (720-((position.y* IMap.ZOOM) - (camera.position.y*IMap.ZOOM))));
		spriter.update();
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Build input variable
	private int buildInput() {
		oldPlayerInput = playerInput;
		playerInput = 0;
		if(MouseFocus.getFocus()==MouseFocus.WindowFocus.InputWindow)
			return 0;

// Speed Cheat
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
			playerInput |= bitSpeedCheat;
		}
// Fire Button
		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			playerInput |= bitFireButton;
		}
// Pressed left
		if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			playerInput |= bitLeft;
		} else {
// Pressed right
			if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				playerInput |= bitRight;
			}
		}
// Pressed up
		if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
			playerInput |= bitUp;
		} else {
// Pressed down
			if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				playerInput |= bitDown;
			}
		}

		return (playerInput);
	}

	private void testErase(float fx, float fy){

		int x = (int) ((fx) * IMap.ZOOM) / 8;
		int y = (int) ((fy+16) * IMap.ZOOM) / 8;
		short[][] map = MapGeneration.getForegroundMap();
		x--;
		y--;
		for (int x2=0; x2<4; x2++) {
			for (int y2 = 0; y2 < 4; y2 ++) {
				map[x+x2][y+y2] = 0;
			}
		}
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void movePlayerHorizontal() {
		if ((playerInput & bitLeft) != 0) {
			movePlayerLeft();
		} else {
			if ((playerInput & bitRight) != 0) {
				movePlayerRight();
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void movePlayerLeft() {
		if(playerFacingRight) {
			spriter.flipX();
			flipAttachments();
			playerFacingRight=false;
		}


		if (checkTiles(position.x-playerBaseSpeed,position.y)==true) {
			position.x -= playerBaseSpeed;
		} else {
			if (checkTiles(position.x-playerBaseSpeed,position.y-(stepUpValue*1.5f))==true) {
				position.x -= playerBaseSpeed;
				position.y -= stepUpValue;
				clampYToFloor();
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void movePlayerRight() {
		if(!playerFacingRight) {
			spriter.flipX();
			flipAttachments();
			playerFacingRight=true;
		}
		if (checkTiles(position.x+playerBaseSpeed,position.y)==true) {
			position.x += playerBaseSpeed;
		} else {
			if (checkTiles(position.x+playerBaseSpeed,position.y-(stepUpValue*1.5f))==true) {
				position.x += playerBaseSpeed;
				position.y -= stepUpValue;
				clampYToFloor();
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void checkForJump(){
		if ((playerInput&bitUp)!=0 && (oldPlayerInput&bitUp)==0) {
			doubleJumped=false;
			playerStatus = p_Actions.p_Jump;
			playerJumpIndex = 0;
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void checkForFall() {
		if (!playerCheckFloor()) {
			startFall();
		}
	}

	private void startFall() {
		playerFallSpeed = playerFallStartSpeed;
		playerStatus = p_Actions.p_Fall;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean playerCheckFloor() { return (!checkTiles(position.x, position.y + 13)); }

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean playerBumpedHead() { return (!checkTiles(position.x, position.y - 13)); }


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean checkTiles(float fx,float fy) {
		int x=(int) ((fx-7)*IMap.ZOOM) / 8;
		int y=(int) ((fy+(13*2))*IMap.ZOOM) / 8;
		short [][] map = MapGeneration.getForegroundMap();
		if (getTileId (map [x][y]) == 0 && getTileId (map [x+1][y]) == 0 && getTileId (map [x][y-1]) == 0 && getTileId (map [x+1][y-1]) == 0 && getTileId (map [x][y-2]) == 0 && getTileId (map [x+1][y-2]) == 0) {
			return (true);
		}
		return (false);
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Clamp player to map bounds

	private void clampPlayerToMap() {
		if (position.x < 32f) 							{ position.x = 32f; }
		if ((position.x*IMap.ZOOM) > (IMap.WIDTH-3)*8) 	{ position.x = ((IMap.WIDTH-3)*8)/IMap.ZOOM; }
		if (position.y < 64f) 							{ position.y = 64f; }
		if ((position.y*IMap.ZOOM) > (IMap.HEIGHT-4)*8) { position.y = ((IMap.HEIGHT-4)*8)/IMap.ZOOM; }
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void clampYToFloor() {
		position.y = (((int) position.y)&-7)|7;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Returns current biome as an int.

//	forestBiome 0
//	jungleBiome 1
//	arcticBiome 2
//	desertBiome 3

	public static int getCurrentBiome(Actor player) {
		int eigth = (int) (((IMap.WIDTH-3)*8)/IMap.ZOOM)/8; 		// Maximum right position of player
		if (player.position.x>(eigth*7)) { return biomeList[7]; }
		if (player.position.x>(eigth*6)) { return biomeList[6]; }
		if (player.position.x>(eigth*5)) { return biomeList[5]; }
		if (player.position.x>(eigth*4)) { return biomeList[4]; }
		if (player.position.x>(eigth*3)) { return biomeList[3]; }
		if (player.position.x>(eigth*2)) { return biomeList[2]; }
		if (player.position.x>(eigth*1)) { return biomeList[1]; }
		return biomeList[0];
	}

	private static int[] biomeList = {0,1,2,3,2,1,0,3};
}