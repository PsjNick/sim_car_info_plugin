class SMS {
  /// The body of the message received
  final String body;

  /// Senders contact
  final String senderNumber;

  /// The time when sms is received
  final DateTime timeReceived;

  /// 卡号
  final int slot;

  /// 发件人名称
  final String contactName;

  SMS({
    required this.body,
    required this.senderNumber,
    required this.timeReceived,
    required this.slot,
    required this.contactName,
  });

  /// creates an SMS instance from the list of objects
  /// received from the broadcast stream of event channel
  SMS.fromList(List data)
      : body = data[0] as String,
        senderNumber = data[1] as String,
        timeReceived = DateTime.fromMillisecondsSinceEpoch(
          int.parse(data[2] as String),
        ),
        slot = data[3],
        contactName = data[4];
}
