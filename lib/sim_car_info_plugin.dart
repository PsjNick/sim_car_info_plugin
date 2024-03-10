
import 'package:permission_handler/permission_handler.dart';
import 'package:sim_car_info_plugin/model/sms.dart';

import 'sim_car_info_plugin_platform_interface.dart';

class SimCarInfoPlugin {

  Future reqPersmissions() async{

    await Permission.sms.request();
    await Permission.phone.request();
    await Permission.contacts.request();

  }

  Future simCarInfo() async{
    return SimCarInfoPluginPlatform.instance.simCarInfo();
  }

  Stream<SMS>  startListen() {
    return SimCarInfoPluginPlatform.instance.startListen();
  }

  stopListen() {
    return SimCarInfoPluginPlatform.instance.stopListen();
  }

}
