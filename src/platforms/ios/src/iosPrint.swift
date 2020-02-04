import UIKit

protocol PrinterDeLegate: class {
  func received(message: String)
}

class IOSPrinter: NSObject {
  var inputStream: InputStream!
  var outputStream: OutputStream!
  
  weak var delegate: PrinterDeLegate?
  
  let maxReadLength = 4096
  
  @objc dynamic open func setupNetworkCommunication(servername: String, port: UInt32 ) {
    var readStream: Unmanaged<CFReadStream>?
    var writeStream: Unmanaged<CFWriteStream>?
    CFStreamCreatePairWithSocketToHost(kCFAllocatorDefault,
                                       servername as CFString,
                                       port,
                                       &readStream,
                                       &writeStream)
    
    inputStream = readStream!.takeRetainedValue()
    outputStream = writeStream!.takeRetainedValue()
    
    inputStream.delegate = self
    
    inputStream.schedule(in: .current, forMode: .common)
    outputStream.schedule(in: .current, forMode: .common)
    
    inputStream.open()
    outputStream.open()
  }

  @objc dynamic open func sendMessage(message: String) {
    let data = "\(message)".data(using: .utf8)!
    _ = data.withUnsafeBytes {
      guard let pointer = $0.baseAddress?.assumingMemoryBound(to: UInt8.self) else {
        print("Error joining chat")
        return
      }
      outputStream.write(pointer, maxLength: data.count)
    }
  }
  
  @objc dynamic open func closePrinter() {
    inputStream.close()
    outputStream.close()
  }
}

extension IOSPrinter: StreamDelegate {
  func stream(_ aStream: Stream, handle eventCode: Stream.Event) {
    switch eventCode {
    case .hasBytesAvailable:
      print("new message received")
      readAvailableBytes(stream: aStream as! InputStream)
    case .endEncountered:
      print("new message received")
      closePrinter()
    case .errorOccurred:
      print("error occurred")
    case .hasSpaceAvailable:
      print("has space available")
    default:
      print("some other event...")
    }
  }
  
  private func readAvailableBytes(stream: InputStream) {
    let buffer = UnsafeMutablePointer<UInt8>.allocate(capacity: maxReadLength)
    while stream.hasBytesAvailable {
      let numberOfBytesRead = inputStream.read(buffer, maxLength: maxReadLength)
      
      if numberOfBytesRead < 0, let error = stream.streamError {
        print(error)
        break
      }
      
      // Construct the message object
      if let message = processedMessageString(buffer: buffer, length: numberOfBytesRead) {
        // Notify interested parties
        delegate?.received(message: message)
      }
    }
  }
  
  private func processedMessageString(buffer: UnsafeMutablePointer<UInt8>,
                                      length: Int) -> String? {
    guard
      let stringArray = String(
        bytesNoCopy: buffer,
        length: length,
        encoding: .utf8,
        freeWhenDone: true)?.components(separatedBy: ":"),
      let message = stringArray.last
      else {
        return nil
    }
    return message
  }
}
