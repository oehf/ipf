#!/usr/bin/env python3
"""
Sends an HL7v2 ADT^A01 message (IHE ITI-8 Patient Identity Feed) over MLLP and prints the acknowledgement.

Usage: python3 send-adt.py [host] [port]      (defaults: localhost 2575)
"""
import socket
import sys

host = sys.argv[1] if len(sys.argv) > 1 else "localhost"
port = int(sys.argv[2]) if len(sys.argv) > 2 else 2575

# ITI-8 uses HL7 v2.3.1. Segments are separated by carriage returns.
message = "\r".join([
    "MSH|^~\\&|SOURCE_APP|SOURCE_FAC|PIX_MGR|PIX_FAC|20261128120000||ADT^A01|MSG0001|P|2.3.1",
    "EVN|A01|20261128120000",
    "PID|||4711^^^&1.2.3.4&ISO||Doe^John||19700101|M",
    "PV1||O",
]) + "\r"

# MLLP framing: <VT> message <FS><CR>
START, END = b"\x0b", b"\x1c\r"

with socket.create_connection((host, port), timeout=10) as connection:
    connection.sendall(START + message.encode() + END)
    response = b""
    while not response.endswith(END):
        chunk = connection.recv(4096)
        if not chunk:
            break
        response += chunk

print(response.strip(START + END).decode().replace("\r", "\n"))
