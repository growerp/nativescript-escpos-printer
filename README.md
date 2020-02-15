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
  import { Client } from 'nativescript-escpos-printer';
  if (!client.print(servername, port, message))
    alert("printer error...")
```

## API

## License

Apache License Version 2.0, January 2004
