# nativescript-ichi-printer

POS printer for NativeScript.

## Supported platforms

- Android (any device with Android 4.4 and higher)

There is no support for iOS yet!

## Installing

```
tns plugin add nativescript-ichi-printer
```

## Usage

Here is a TypeScript example:

```js
import {PrintClient} from "nativescript-ichi-printer";

// new Printer Client, param:  0: TCP, 1: USB, 2: Bluetooth
var printClient = new PrintClient(0);
printClient.onData = (data: Array<number>) => {
    console.log("Data from Printer: ", data);
};
printClient.onError = (id: number, message: string) => {
    console.log("Print client error for action #", id, ": ", message);
};
printClient.onConnected = (id: number) => {
    console.log("Print client connected action #: ", id);
    var message = "Print test String!";
    var bytes = [];
    for (var i = 0; i < message.length; i++) {
        var c = message.charCodeAt(i);
        bytes.push(c & 0xFF);
    }
    printClient.send(bytes);
};
printClient.onSent = (id: number) => {
    console.log("Print client sent action #: ", id);
    // When we are finished
    printClient.close();
};
printClient.onClosed = (id: number) => {
    console.log("Print client closed action #: ", id);
};

// Connect printer (type: TCP), param: IP, port
printClient.connect("192.168.1.192", 9100);

// // Connect printer (type: USB), param: printer Name by getUsbPrinters()
// printClient.connect("USB Support Printer 1", 0);

// // Connect printer (type: Bluetooth), param: printer Name by getBlueToothPrinters()
// printClient.connect("58 POS printer", 0);


```



