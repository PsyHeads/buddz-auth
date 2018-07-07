INSERT INTO oauth_client_details
    (client_id, client_secret, scope, authorized_grant_types,
    web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information, autoapprove)
VALUES
    ('buddz-web', 'buddz-web-secret', 'foo,read,write', 'password,authorization_code,refresh_token', null, 'ROLE_CLIENT', 36000, 36000, null, 'true');
