package ARDrone;

import org.douxiao.akj_uav.Constant;

//
public class ARDroneAPI {
//
    private ARDrone ardrone;

    public String flag;
//
    public ARDroneAPI() throws Exception {
        ardrone = new ARDrone(Constant.DRONE_IP);
//        this.flag = this.flag_list[0];
    }
//    public String getFlightState(){
//        if(ardrone.getState_flag()==1){
//            this.flag = this.flag + "失败！";
//        }else{
//            this.flag = this.flag + "成功！"+ardrone.getFlight_msg();
//        }
//        return this.flag;
//    }
//
//    public String getStatus() {
//        return this.flag;
//    }
//
    public void landing() {  //着陆函数
        try {
            ardrone.send_at_cmd("AT*REF="
                    + ardrone.get_seq()
                    + ",290717696");
//            this.flag = this.flag_list[3];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hovering() { //悬停函数
        try {
            ardrone.send_pcmd(0, 0, 0, 0, 0);
//            this.flag = this.flag_list[10];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takeoff() { //起飞函数
        try {
            ardrone.send_at_cmd("AT*REF="
                    + ardrone.get_seq()
                    + ",290718208");
//            this.flag = this.flag_list[2];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disbleEmergency() { //紧急函数
        try {
            ardrone.send_at_cmd("AT*REF="
                    + ardrone.get_seq()
                    + ",290717952");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trim() {
        try {
            ardrone.send_at_cmd("AT*FTRIM=" + ardrone.get_seq()); //flat trim
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void up() { //上升函数
        try {
            ardrone.send_pcmd(1, 0, 0, ardrone.getSpeed(), 0);
//            this.flag = this.flag_list[4];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void down() { //下降函数
        try {
            ardrone.send_pcmd(1, 0, 0, -ardrone.getSpeed(), 0);
//            this.flag = this.flag_list[5];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rotatel() {//向左偏转
        try {
            ardrone.send_pcmd(1, 0, 0, 0, -ardrone.getSpeed());
//            this.flag = this.flag_list[7];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rotater() { //向右偏转
        try {
            ardrone.send_pcmd(1, 0, 0, 0, ardrone.getSpeed());
//            this.flag = this.flag_list[6];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goForward() { //前进函数
        try {
            ardrone.send_pcmd(1, ardrone.getSpeed(),0,0,0);
//            this.flag = this.flag_list[8];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBackward() { //后退函数
        try {
            ardrone.send_pcmd(1, -ardrone.getSpeed(),0,0,0);
//            this.flag = this.flag_list[9];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
