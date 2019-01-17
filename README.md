# HaveIBeenPwned Password Policy for Keycloak

## Intro

[Apache Keycloak](https://www.keycloak.org/about.html) is an open source IDAM solution supporting OpenID Connect, OAuth 2.0, and SAML.

Have I Been Pwned (HIBP) is a service that aims to inform people if their email addresses or passwords have been present in any known security breaches.

This project implements a [Service Provider Interface (SPIs)](https://www.keycloak.org/docs/latest/server_development/index.html#_providers) to block the usage of passwords present in data breaches, 
using the [HIBP Password API](https://haveibeenpwned.com/API/v2#PwnedPasswords).

## Installation

The Dockerfile provides the necessary installation and registration steps, which match those [documented by Keycloak](https://www.keycloak.org/docs/latest/server_development/index.html#registering-provider-implementations).

Using the `Keycloak Deployer`, the only step required is copying the build jar into `standalone/deployments/` within Keycloak.

## Configuration

Once registered, the Policy can be added via `Authentication -> Password Policy` and selecting `HIBP Data Breaches` from the Policy dropdown.

The `Policy Value` is the maximum number of breaches a password can have occurred in to be accepted, defaulting to `0`.