import json

class Message:
    def __init__(self, methodId=None):
        self.messageType = None
        self.requestId = None
        self.methodId = methodId
        self.arguments = None

    def setMessageType(self, messageType):
        self.messageType = messageType

    def setRequestId(self, requestId):
        self.requestId = requestId

    def setMethodId(self, methodId):
        self.methodId = methodId

    def setArguments(self, arguments):
        self.arguments = arguments

    def getMessageType(self):
        return self.messageType

    def getParams(self):
        return self.arguments

    def to_json(self):
        """Converte o objeto Message para uma string JSON."""
        return json.dumps({
            "messageType": self.messageType,
            "requestId": self.requestId,
            "methodId": self.methodId,
            "arguments": self.arguments
        })

    @staticmethod
    def from_json(json_str):
        """Cria um objeto Message a partir de uma string JSON."""
        data = json.loads(json_str)
        msg = Message(data["methodId"])
        msg.setMessageType(data["messageType"])
        msg.setRequestId(data["requestId"])
        msg.setArguments(data["arguments"])
        return msg

