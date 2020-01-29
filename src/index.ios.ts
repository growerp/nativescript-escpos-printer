

export class PrintClient {

    public onData: {(data: string): void;};
    public onError: {(id: number, message: string): void;};
    public onConnected: {(id: number): void;};
    public onSent: {(id: number): void;};
    public onClosed: {(id: number): void;};


    public connect(servername: string, port: number): number {
        throw "Not implemented";
    }

    public close(): number {
        throw "Not implemented";
    }

    public send(data: Array<number>): number {
        throw "Not implemented";
    }

    public receive(): number {
        throw "Not implemented";
    }
}
