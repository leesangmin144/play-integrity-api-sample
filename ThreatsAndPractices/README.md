# Standard Request
Ongoing...
https://developer.android.com/google/play/integrity/standard

# Classic Request
## Security Threats
### Threat 1
- References: https://developer.android.com/google/play/integrity/classic#include-request

### Threat 2
- References: https://developer.android.com/google/play/integrity/classic#include-unique

### Threat 3
- References: https://developer.android.com/google/play/integrity/verdict#decrypt-verify-locally

## Best Practices
### Comply with the recommendations of Google Docs
- References: https://developer.android.com/topic/security/best-practices

### Use an appropriate nonce and sign the data that needs to be protected
- References: https://developer.android.com/google/play/integrity/classic#combine-protections

![class-diagram](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/leesangmin144/play-integrity-api-sample/main/ThreatsAndPractices/sequence1.puml)
- #3 globally-unique value
    - A secure random value that cannot be predicted from the previous value must be generated. (e.g. CSPRNG)
- #6 Hash of message to protect
    - A standard-comliant secure hash algorithm must be used. (e.g. SHA-2, SHA-3)
- #7, 9
    - Use the hash value created in step 6 with a unique value received from the server as a parameter for setNonce(). (e.g. hash || uniqueValue)
    - At this time, the parameter for setNonce() is sent to the Google Server, so it must not contain any sensitive data.
- #16, 17, 18
    - The verification result of the token should be logged, and automated monitoring should be used to detect miseuse or abuse, and incident response should be carried out.