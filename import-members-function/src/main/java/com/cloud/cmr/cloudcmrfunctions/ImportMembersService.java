package com.cloud.cmr.cloudcmrfunctions;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Service
public class ImportMembersService {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final GcpStorageResourceLoader loader;
    private final MemberGateway memberGateway;

    public ImportMembersService(GcpStorageResourceLoader loader, MemberGateway memberGateway) {
        this.loader = loader;
        this.memberGateway = memberGateway;
    }

    public void importMemberFromGcpStorage(String bucket, String name) {
        Resource resource = loader.loadResource(bucket, name);
        try (var inputStream = resource.getInputStream();
             var isr = new InputStreamReader(inputStream);
             var reader = new BufferedReader(isr)) {
            var completables = reader.lines()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(this::toMemberDTO)
                    .map(memberGateway::send)
                    .map(ListenableFuture::completable)
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(completables).join();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read the file [" + resource.getFilename() + "]", e);
        }
    }

    private MemberDTO toMemberDTO(String line) {
        String[] data = line.split(";");
        String licenceNumber = data[0];
        String firstName = data[3];
        String lastName = data[2];
        String gender = data[4];
        LocalDate birthDate = LocalDate.parse(data[6], format);
        String email = data[15];
        String phone = data[13];
        String mobile = data[14];
        AddressDTO address = new AddressDTO(
                data[7],
                data[8],
                data[9],
                data[10],
                data[11]
        );
        var member = new MemberDTO(
                licenceNumber,
                firstName,
                lastName,
                gender,
                birthDate,
                email,
                phone,
                mobile,
                address
        );
        return member;
    }
}
