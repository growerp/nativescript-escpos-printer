import { PrintClient } from 'nativescript-escpos-printer';
import { isIOS, isAndroid } from 'tns-core-modules/platform';

export function onTap() {
    var printClient = new PrintClient(0);
    var message = "Print test String!";
    printClient.print(isIOS? 'localhost' : '10.0.2.2', 9100, message)    
}

import { NavigatedData, Page } from "tns-core-modules/ui/page";

import { HomeViewModel } from "./home-view-model";

export function onNavigatingTo(args: NavigatedData) {
    const page = <Page>args.object;

    page.bindingContext = new HomeViewModel();
}
