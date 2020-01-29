import { PrintClient } from 'nativescript-escpos-printer';

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

console.log("printing now......")
printClient.connect("10.0.2.2", 9100);


import { NavigatedData, Page } from "tns-core-modules/ui/page";

import { HomeViewModel } from "./home-view-model";

export function onNavigatingTo(args: NavigatedData) {
    const page = <Page>args.object;

    page.bindingContext = new HomeViewModel();
}
