import 'dart:convert';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:sim_car_info_plugin/sim_car_info_plugin.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _simCarInfoPlugin = SimCarInfoPlugin();

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Plugin example app'),
          ),
          body: ListView(
            children: [
              TextButton(
                onPressed: () async {
                  _simCarInfoPlugin.reqPersmissions();
                },
                child: Text('Permission'),
              ),
              TextButton(
                onPressed: () async {

                  var info = await _simCarInfoPlugin.simCarInfo();

                  print("获取到Sim卡 info: ");
                  print(info);

                },
                child: Text('SimCarInfo'),
              ),
            ],
          )),
    );
  }
}
