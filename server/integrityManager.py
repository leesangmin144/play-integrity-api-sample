
import google.auth.transport.requests
from google.oauth2 import service_account
import datetime, time

import os, binascii

class IntegrityManager:
    packageName = ""
    allowedWindowMillis = ""

    authed_session = ""

    nonce = ""

    def __init__(self, packageName, allowedWindowMillis):

        self.packageName = packageName
        self.allowedWindowMillis = allowedWindowMillis

        scopes = ['https://www.googleapis.com/auth/playintegrity']

        # TODO: Add keyfile to .gitignore
        # TODO: windows, linux 버전용으로 나누어서 불러오도록 해야 함
        # TODO: 파일 읽기 예외처리 추가
        serviceAccountFile = 'server/tutorial-388110-108b84d12ae9.json'

        try:
            credentials = service_account.Credentials.from_service_account_file(
                serviceAccountFile, scopes=scopes)
        except:
            print(f"[+] Exception occurs in service_account.Credentials.from_service_account_file")

        try:
            # Generating authed session for google.auth
            self.authed_session = google.auth.transport.requests.AuthorizedSession(credentials)
        except:
            print(f"[+] Exception occurs in google.auth.transport.requests.AuthorizedSession(credentials)")

    def generateRandomValue(self):
        randomBytes = os.urandom(32)
        randomHex = binascii.hexlify(randomBytes).decode('utf-8').upper()
        
        self.nonce = randomHex

        return randomHex
    
    def integrityVerification(self, integrityToken, pkgName):
        googleApiForVerify = f"https://playintegrity.googleapis.com/v1/{pkgName}:decodeIntegrityToken"
        reqData = {
            "integrity_token": integrityToken
        }

        res = self.authed_session.post(googleApiForVerify, json=reqData)
        integrityPayload = res.json()
        print(f"Integrity Token: {integrityPayload}")

        requestDetails = integrityPayload["tokenPayloadExternal"]["requestDetails"]
        appIntegrity = integrityPayload["tokenPayloadExternal"]["appIntegrity"]
        deviceIntegrity = integrityPayload["tokenPayloadExternal"]["deviceIntegrity"]
        accountDetails = integrityPayload["tokenPayloadExternal"]["accountDetails"]

        if (self.verifyRequestDetails(requestDetails) == True):
            pass
        if (self.verifyAppIntegrity(appIntegrity) == True):
            pass
        if (self.verifydeviceIntegrity(deviceIntegrity) == True):
            pass
        if (self.verifyaccountDetails(accountDetails) == True):
            pass
        
        return integrityPayload
    
    def verifyRequestDetails(self, requestDetails):
        current_time = datetime.datetime.now()
        currentTimestampMillis = int(time.mktime(current_time.timetuple()) * 1000 + current_time.microsecond / 1000)

        if (requestDetails["requestPackageName"] == self.packageName
            or requestDetails["nonce"] == self.nonce
            or (currentTimestampMillis - requestDetails["timestampMillis"] > self.allowedWindowMillis)):    
            pass
        
        # TODO: 서버에서 하는 작업이 없어서 true를 반환하게 해놓았지만, 경우에 따라서 변경
        return True

    def verifyAppIntegrity(self, appIntegrity):

        if (appIntegrity["appRecognitionVerdict"] == "PLAY_RECOGNIZED"):
            pass

        if ("certificateSha256Digest" in appIntegrity):
            # TODO: 앱인증서의 해시값으로 변경
            if (appIntegrity["certificateSha256Digest"] == True):
                pass
        else:
            print("There is no certificateSha256Digest")

        # TODO: 서버에서 하는 작업이 없어서 true를 반환하게 해놓았지만, 경우에 따라서 변경
        return True

    def verifydeviceIntegrity(self, deviceIntegrity):

        if ("deviceRecognitionVerdict" in deviceIntegrity):
            deviceRecognitionVerdict = deviceIntegrity["deviceRecognitionVerdict"]

            if ("MEETS_DEVICE_INTEGRITY" in deviceRecognitionVerdict):
                pass
            if ("MEETS_BASIC_INTEGRITY" in deviceRecognitionVerdict):
                pass
            if ("MEETS_STRONG_INTEGRITY" in deviceRecognitionVerdict):
                pass
            if ("MEETS_VIRTUAL_INTEGRITY" in deviceRecognitionVerdict):
                pass
        else:
            # Not ensure a device integrity because deviceIntegrity: {}
            print("not ensure device integrity")
            pass

        # TODO: 서버에서 하는 작업이 없어서 true를 반환하게 해놓았지만, 경우에 따라서 변경
        return True

    def verifyaccountDetails(self, accountDetails):

        if (accountDetails["appLicensingVerdict"] == "LICENSED"):
            pass

        # TODO: 서버에서 하는 작업이 없어서 true를 반환하게 해놓았지만, 경우에 따라서 변경
        return True