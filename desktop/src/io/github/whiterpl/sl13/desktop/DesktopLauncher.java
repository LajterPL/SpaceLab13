package io.github.whiterpl.sl13.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.whiterpl.sl13.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = true;
		config.width = 1920;
		config.height = 1080;
		config.addIcon("sprites/ico.jpg", Files.FileType.Internal);
		config.title = "Space Lab 13";
		new LwjglApplication(Game.getInstance(), config);
	}
}
