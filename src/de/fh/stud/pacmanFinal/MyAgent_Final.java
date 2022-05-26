package de.fh.stud.pacmanFinal;

import de.fh.kiServer.agents.Agent;
import de.fh.pacman.PacmanAgent;
import de.fh.pacman.PacmanGameResult;
import de.fh.pacman.PacmanPercept;
import de.fh.pacman.PacmanStartInfo;
import de.fh.pacman.enums.PacmanAction;
import de.fh.pacman.enums.PacmanActionEffect;

public class MyAgent_Final extends PacmanAgent {
	// Pommes
	public MyAgent_Final(String name) {
		super(name);
	}
	
	public static void main(String[] args) {
		MyAgent_Final agent = new MyAgent_Final("MyAgent");
		Agent.start(agent, "127.0.0.1", 5000);
	}

	@Override
	public PacmanAction action(PacmanPercept percept, PacmanActionEffect actionEffect) {
		
		return PacmanAction.GO_EAST;
	}

	@Override
	protected void onGameStart(PacmanStartInfo startInfo) {
		
	}

	@Override
	protected void onGameover(PacmanGameResult gameResult) {
		
	}
	
}
