import {
  RSocketClient,
  JsonSerializer,
  IdentitySerializer,
  encodeAndAddCustomMetadata,
  encodeAndAddWellKnownMetadata,
} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import { JsonMetadataSerializer } from "./Metadata";

const wsURL = "ws://localhost:9900/api/notification/nrws";

class RSocketService {
  bearerToken(token) {
    const buffer = Buffer.alloc(1 + token.length);
    buffer.writeUInt8(0 | 0x80, 0);
    buffer.write(token, 1, "utf-8");
    return buffer;
  }

  getRSocketClient = () => {
    return new RSocketClient({
      serializers: {
        data: JsonSerializer,
        metadata: JsonMetadataSerializer,
      },
      setup: {
        keepAlive: 60000,
        lifetime: 180000,
        dataMimeType: "application/json",
        metadataMimeType: JsonMetadataSerializer.MIME_TYPE,
      },
      transport: new RSocketWebSocketClient({
        url: wsURL,
      }),
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
