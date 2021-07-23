import { RSocketClient, JsonSerializer } from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import { JsonMetadataSerializer } from "./Metadata";

const wsURL = "ws://localhost:9900/api/notification/nrws";

class RSocketService {
  getRSocketClient = () => {
    return new RSocketClient({
      serializers: {
        data: JsonSerializer,
        metadata: JsonMetadataSerializer,
      },
      setup: {
        // ms btw sending keepalive to server
        keepAlive: 10000,
        // ms timeout if no keepalive response
        lifetime: 20000,
        dataMimeType: "application/json",
        metadataMimeType: JsonMetadataSerializer.MIME_TYPE,
      },
      transport: new RSocketWebSocketClient({
        url: wsURL,
      }),
      responder: (r) => console.log(r),
    });
  };

  getRSocketConnection = () => {
    return new Promise((resolve, reject) => {
      this.getRSocketClient()
        .connect()
        .then(
          (socket) => resolve(socket),
          (err) => reject(err)
        );
    });
  };
}

const instance = new RSocketService();
export default instance;
