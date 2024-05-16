public class Tetris {
    abstract class Tetrominos{
        //상속 클래스
        //x y 시작 표시, r 회전, w h블럭 배열의 크기
        //type 블럭의 색상, numOfBlock 블럭 회전시 나타날 모양의 수
        protected int x;
        protected int y;
        protected int r;
        protected int w;
        protected int h;
        protected int type;
        protected int numOfBlockType;
        protected int [][][] block;
        public Tetrominos(){}
        public void rotate(){
            r = (r+1)%numOfBlockType;
        }
        public void preRotate(){
            r = (r-1+numOfBlockType) % numOfBlockType;
        }
    }
}
