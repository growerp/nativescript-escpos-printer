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
import { Printer } from 'nativescript-escpos-printer';
import { isIOS } from 'tns-core-modules/platform';
var printer = new Printer
printer.print(isIOS? 'localhost' : '10.0.2.2', 9100, "Print test String!")
.then( result => {
    if (!result) alert("printer error!")
})
```

## API

## License

Apache License Version 2.0, January 2004

## known problems
in IOS, the app will get stuck when a printer is defined, but not found or switched off.
(looks like outputStream.hasSpaceAvailable in IOS swift, always returns false, even if connected)
