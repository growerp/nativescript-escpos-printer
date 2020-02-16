/*

generate meta data in demo dir:
TNS_DEBUG_METADATA_PATH="$(pwd)/metadata" tns build ios

Generate typings in demo dir:
TNS_TYPESCRIPT_DECLARATIONS_PATH="$(pwd)/typings" tns build ios

then copy objc!nsswiftsupport.d.ts to src/platforms/ios/typings
and add the line:
/// <reference path="./platforms/ios/typings/objc!nsswiftsupport.d.ts"/>
to the file: references.d.ts

*/



export class PrintClient {

    public print(servername: string, port: number, data: string): boolean {
        var printer = new IOSPrinter()
        printer.setupNetworkCommunicationWithServernamePort(servername, port)
        printer.sendMessageWithMessage(data)
        printer.closePrinter()
        return true
    }
}
