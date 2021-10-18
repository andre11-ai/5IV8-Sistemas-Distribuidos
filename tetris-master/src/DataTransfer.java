/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Studio
 */
public final class DataTransfer {
    private static final DataTransfer instance = new DataTransfer();
    
    private static final int estado = 0, rival = 1, lineas_a_recivir = 2;
    
    public static final int esperando_rival = 0, jugando = 1, derrota = 2, ganar = 3;
            
    private int limit, playerCount, playerData[][];
    private boolean instantiated = false;
    
    private void init() {
        if(!instantiated) {
            limit = 100;
            playerCount = 0;
            playerData = new int[limit][3];
            for (int i = 0; i < limit; i++)
                for(int j = 0; j < 3; j++)
                    playerData[i][j] = -1;
            instantiated = true;
        }
    }
    
    public static DataTransfer getInstance() {
        return instance;
    }
    
    public int addPlayer() {
        init();
        if(playerCount < limit) {
            int playerId = -1, state = 0, enemy = -1, linesToReceive = 0;
            for(int i = 0; i < limit; i++) {
                if (playerData[i][estado] == -1) {
                    playerId = i;
                    playerData[playerId] = new int[]{state, enemy, linesToReceive};
                    playerData[i][estado] = esperando_rival;
                    break;
                }
            }
            if (playerId > -1) {
                findEnemy(playerId);
                playerCount++;
                return playerId;
            }
        } 
        return -1;
    }
    
    public int findEnemy(int playerId) {
        if(playerId > -1) {
            for(int i = 0; i < limit; i++) {
                if(i == playerId) continue;
                if (playerData[i][estado] == 0) {
                    playerData[i][estado] = 1;
                    playerData[i][rival] = playerId;
                    playerData[playerId][estado] = 1;
                    playerData[playerId][rival] = i;
                    return i;
                } else {
                    continue;
                }
            }
        }
        return -1;
    }
    
    public void freePlayer (int playerId) {
        if(playerId > -1) {
            int enemy = playerData[playerId][rival];
            if (enemy > -1) {
                playerData[enemy][estado] = ganar;
                playerData[enemy][rival] = -1;
            }
            for(int j = 0; j < 3; j++)
                playerData[playerId][j] = -1;
            playerCount--;
        }
    }
    
    public int getEnemy(int playerId) {
        if(playerId > -1) {
            return playerData[playerId][rival];
        }
        return -1;
    }
    
    public int getState(int playerId) {
        if(playerId > -1) {
            return playerData[playerId][estado];
        } else {
            return 0;
        }
    }
    
    public int receiveLines(int playerId) {
        if(playerId > -1) {
            int lines = playerData[playerId][lineas_a_recivir];
            playerData[playerId][lineas_a_recivir] = 0;
            return lines;
        }
        return 0;
    }
    
    public void sendLines(int playerId, int lines) {
        if(playerId > -1) {
            int enemy = playerData[playerId][rival];
            if(enemy > -1) playerData[enemy][lineas_a_recivir] += lines;
        }
    }
    
    public void setState(int playerId, int state) {
        if(playerId > -1) playerData[playerId][estado]  = state;
    }
}

