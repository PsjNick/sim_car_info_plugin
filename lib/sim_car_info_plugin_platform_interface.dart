import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:sim_car_info_plugin/model/sms.dart';

import 'sim_car_info_plugin_method_channel.dart';

abstract class SimCarInfoPluginPlatform extends PlatformInterface {

  SimCarInfoPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static SimCarInfoPluginPlatform _instance = MethodChannelSimCarInfoPlugin();

  static SimCarInfoPluginPlatform get instance => _instance;

  static set instance(SimCarInfoPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future reqPersmissions() {
    throw UnimplementedError('reqPersmissions() has not been implemented.');
  }

  Future simCarInfo() {
    throw UnimplementedError('simCarInfo() has not been implemented.');
  }

  Stream<SMS> startListen() {
    throw UnimplementedError('simCarInfo() has not been implemented.');
  }

  stopListen() {
    throw UnimplementedError('simCarInfo() has not been implemented.');
  }



}
