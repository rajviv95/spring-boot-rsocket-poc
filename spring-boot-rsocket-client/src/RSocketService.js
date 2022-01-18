import {
  RSocketClient,
  JsonSerializer,
  MESSAGE_RSOCKET_COMPOSITE_METADATA,
} from "rsocket-core";
import RSocketWebSocketClient from "rsocket-websocket-client";
import { Metadata as M1, JsonMetadataSerializer } from "./Metadata";
//import Metadata from "./metadatanew";
//import Metadata from "./MetadataThree";

//const wsURL = "ws://localhost:9900/api/notification/nrws";
//const wsURL = "ws://localhost:7040/api/notification/rsocket";
const wsURL = "ws://localhost:9900/api/notification/nrws";

class RSocketService {
  getRSocketClient = () => {
      /*
    const metadata = new Metadata({
      route: "setup",
      auth: { type: "bearer", token: "SETUP TOKEN" },
    }).toMetadata();

    let metadata1 = {
      route: "setup",
      "message/x.rsocket.authentication.v0": "SETUP TOKEN 1",
    };
    */

    let m1 = new M1();
    m1.set(M1.ROUTE, "setup");
    m1.set(M1.AUTHENTICATION_BEARER, "d9ce6c71-8fd0-4f43-a64b-094e80a9e038");

    console.log(m1);

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
        metadataMimeType: "application/vnd.spring.rsocket.metadata+json", //MESSAGE_RSOCKET_COMPOSITE_METADATA.string, //
        payload: {
          data: undefined,
          metadata: m1,
        },
      },
      transport: new RSocketWebSocketClient({
        url: wsURL,
      }),
      responder: (r) => console.log("responder", r),
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

  getRSocketConnectionSubscribe = () => {
    return new Promise((resolve, reject) => {
      this.getRSocketClient()
        .connect()
        .subscribe({
          onComplete: (socket) => resolve(socket),
          onError: (error) => reject(error),
          onSubscribe: (cancel) => {
            /* call cancel() to abort */
          },
        });
    });
  };
}

const instance = new RSocketService();
export default instance;
