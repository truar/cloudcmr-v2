package com.cloud.cmr.cloudcmrfunctions;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class ImportMembersServiceTest {

    private static final String BUCKET_NAME = "bucket_test";
    private static final String FILE_NAME = "file_name.csv";

    @Test
    void read_member_from_loader_and_send_them_to_a_gateway() {
        GcpStorageResourceLoader loader = mock(GcpStorageResourceLoader.class);
        String s = "EMPTY_HEADER_LINE\n" +
                "PC11737989;;doe;john;M;1967;01/01/1967;1 chemin de la localisation;;Lieu-Dit;12345;Ma Ville;FRANCE;0102030405;0601020304;john.doe@mail.fr;NON;RCA;2018;26/01/2018;19:28:13;26/01/2018;14/10/2018;;PC";
        when(loader.loadResource(BUCKET_NAME, FILE_NAME))
                .thenReturn(new ByteArrayResource(s.getBytes()));
        MemberGateway memberGateway = mock(MemberGateway.class);
        ImportMembersService service = new ImportMembersService(loader, memberGateway);

        service.importMemberFromGcpStorage(BUCKET_NAME, FILE_NAME);

        verify(memberGateway).send(
                new MemberDTO("PC11737989", "john", "doe", "M",
                        LocalDate.of(1967, 1, 1), "john.doe@mail.fr",
                        "0102030405", "0601020304",
                        new AddressDTO("1 chemin de la localisation", "",
                                "Lieu-Dit", "12345", "Ma Ville"))
        );
    }

    @Test
    void read_multiple_members_from_loader_and_send_them_to_a_gateway() {
        GcpStorageResourceLoader loader = mock(GcpStorageResourceLoader.class);
        String s = "EMPTY_HEADER_LINE\n" +
                "PC11737989;;doe;john;M;1967;01/01/1967;1 chemin de la localisation;;Lieu-Dit;12345;Ma Ville;FRANCE;0102030405;0601020304;john.doe@mail.fr;NON;RCA;2018;26/01/2018;19:28:13;26/01/2018;14/10/2018;;PC\n" +
                "PC11737989;;doe;john;M;1967;01/01/1967;1 chemin de la localisation;;Lieu-Dit;12345;Ma Ville;FRANCE;0102030405;0601020304;john.doe@mail.fr;NON;RCA;2018;26/01/2018;19:28:13;26/01/2018;14/10/2018;;PC";
        when(loader.loadResource(BUCKET_NAME, FILE_NAME))
                .thenReturn(new ByteArrayResource(s.getBytes()));
        MemberGateway memberGateway = mock(MemberGateway.class);
        ImportMembersService service = new ImportMembersService(loader, memberGateway);

        service.importMemberFromGcpStorage(BUCKET_NAME, FILE_NAME);

        verify(memberGateway, times(2)).send(any(MemberDTO.class));
    }
}
