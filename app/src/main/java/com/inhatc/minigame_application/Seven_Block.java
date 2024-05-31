package com.inhatc.minigame_application;

public class Seven_Block {
    private int[][][] shapes;
    private int currentRotation;//현재의 회전
    private int id;//블럭의 식별자

    public Seven_Block(int id, int[][][] shapes) {//3차원 배열을 받는 생성자
        this.shapes = shapes;
        this.currentRotation = 0;
    }

    public int[][] getCurrentShape(){//현재의 형태를 가져오는 함수
        return shapes[currentRotation];
    }

    public int[][] getNextShape(){
        int nextRotation = (currentRotation + 1) % shapes.length;
        return shapes[nextRotation];
    }

    public void rotate(){
        //회전 현재값에 1을 더한 후 블럭의 길이만큼 나눈 나머지
        currentRotation = (currentRotation + 1) % shapes.length;
    }

    public int getId(){
        return id;
    }

    public static Seven_Block[] createBlocks() {
        Seven_Block[] blocks = new Seven_Block[7];
        //0:I, 1:J, 2:L, 3:O, 4:S, 5:T, 6:Z
        // I Block
        blocks[0] = new Seven_Block(1, new int[][][]{
                {
                        {1, 1, 1, 1},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 1, 0, 0}
                }
        });

        // J Block
        blocks[1] = new Seven_Block(2, new int[][][]{
                {
                        {1, 0, 0, 0},
                        {1, 1, 1, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {0, 1, 1, 0},
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {1, 1, 1, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {1, 1, 0, 0},
                        {0, 0, 0, 0}
                }
        });

        // L Block
        blocks[2] = new Seven_Block(3, new int[][][]{
                {
                        {0, 0, 1, 0},
                        {1, 1, 1, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 1, 1, 0},
                        {0, 0, 0, 0}
                },
                {
                        {1, 1, 1, 0},
                        {1, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {1, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 0}
                }
        });

        // O Block
        blocks[3] = new Seven_Block(4, new int[][][]{
                {
                        {0, 1, 1, 0},
                        {0, 1, 1, 0},
                        {0 ,0 ,0 ,0},
                        {0 ,0 ,0 ,0}
                }
        });

        // S Block
        blocks[4] = new Seven_Block(5, new int[][][]{
                {
                        {0, 1, 1, 0},
                        {1, 1, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {0, 1, 0, 0},
                        {0, 1, 1, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 0}
                }
        });

        // T Block
        blocks[5] = new Seven_Block(6, new int[][][]{
                {
                        {0, 1, 0, 0},
                        {1, 1, 1, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {0, 1, 0, 0},
                        {0, 1, 1, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {1, 1, 1, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {0, 1, 0, 0},
                        {1, 1, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 0}
                }
        });

        // Z Block
        blocks[6] = new Seven_Block(7, new int[][][]{
                {
                        {1, 1, 0, 0},
                        {0, 1, 1, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                },
                {
                        {0, 0, 1, 0},
                        {0, 1, 1, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 0}
                }
        });

        return blocks;
    }
}

