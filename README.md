# nativescript-escpos-printer

Provide a generic printer plugin for ESCPOS thermal printers for android and IOS

This plug is based on:
nativescript-ichi-printer
and ideas used from:
https://www.raywenderlich.com/3437391-real-time-communication-with-streams-tutorial-for-ios#toc-anchor-004

This works for Android and IOS and includes full source (swift/java)

use the included printer simulation to show the printouts.

## Installation

```javascript
tns plugin add nativescript-escpos-printer
```

## Usage

```javascript
import { PrintClient } from 'nativescript-escpos-printer';
import { isIOS } from 'tns-core-modules/platform';
var printClient = new PrintClient
var message = "Print test String!";
printClient.print(
    isIOS? 'localhost' : '10.0.2.2', 9100, message)  
```

## API

## License

Apache License Version 2.0, January 2004
