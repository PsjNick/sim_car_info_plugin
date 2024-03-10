import 'dart:async';
import 'package:flutter/services.dart';
import 'package:sim_car_info_plugin/model/sms.dart';
import 'sim_car_info_plugin_platform_interface.dart';

class MethodChannelSimCarInfoPlugin extends SimCarInfoPluginPlatform {

  final methodChannel = const MethodChannel('sim_car_info_plugin');

  static const _channel = EventChannel("readsms");

  final StreamController _controller = StreamController<SMS>();

  Stream<SMS> get _smsStream => _controller.stream as Stream<SMS>;

  late final StreamSubscription _channelStreamSubscription;

  @override
  Future reqPersmissions() async {
    await methodChannel.invokeMethod<String>('req_persmissions');
  }

  @override
  Future simCarInfo() async {
    var info = await methodChannel.invokeMethod<String>('sim_car_info');
    return info;
  }

  @override
  Stream<SMS> startListen() {

    _channelStreamSubscription = _channel.receiveBroadcastStream().listen((e) {
      if (!_controller.isClosed) {
        _controller.sink.add(SMS.fromList(e));
      }
    });

    return _smsStream;
  }


  @override
  stopListen() {
    _controller.close();
    _channelStreamSubscription.cancel();
  }

}
