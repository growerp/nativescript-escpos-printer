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



class PrintClient {

    public print(servername: string, port: number, data: string): boolean {
        var printer = new IOSPrinter()
        console.log("IOS setup")
        printer.setupNetworkCommunicationWithServernamePort(servername, port)
        console.log("IOS sendmess")
        console.log("result sending message: " + 
            printer.sendMessageWithMessage(data))
        console.log("IOS close")
        printer.closePrinter()
        console.log("IOS done")
        return true
    }
}

export class Printer {
    print(ip: string, port: number, message: string): any {
        let result = 'notYet'
        let count = 0
        console.log("printer start.....ip: " + ip + ' port: ' + port)
        var printClient = new PrintClient();
        printClient.print(ip,port,message)
        result = 'true'

        return (async() => {
            console.log("waiting for result");
            while(result === 'notYet') 
                await new Promise(resolve => setTimeout(resolve, 1000));
            console.log("finished... result: " + result)
            return result==='true'?true:false
        })();
    }
}
